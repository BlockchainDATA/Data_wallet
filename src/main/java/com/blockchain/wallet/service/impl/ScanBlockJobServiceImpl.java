package com.blockchain.wallet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.client.MoneyClient;
import com.blockchain.wallet.entity.*;
import com.blockchain.wallet.enums.*;
import com.blockchain.wallet.service.*;
import com.blockchain.wallet.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 下午1:33
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ScanBlockJobServiceImpl implements IScanBlockJobService {

    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private ITransactionHistoryService transactionHistoryService;

    @Resource
    private IAddressService addressService;

    @Resource
    private IScanBlockConfigService scanBlockConfigService;

    @Resource
    private ITransactionOrderService transactionOrderService;

    @Resource
    private IBlockInfoService blockInfoService;

    @Value("${privateEth.gas-limit}")
    private Long gasLimit;
    @Value("#{'${privateEth.server}'.split(',')}")
    public List<String> ethNodeList;
    @Resource
    private MoneyClient moneyClient;

    @Override
    public void scanBlock(BigInteger blockHeight, ScanBlockConfigEntity scanBlockConfigEntity) {

        EthBlock ethBlockInfo = web3jUtil.scanBlock(blockHeight);
        if (null == ethBlockInfo.getResult()) {
            log.error("Acquisition Block High Failure,Block High:{}", blockHeight);
            return;
        }
        if (CollectionUtils.isEmpty(ethBlockInfo.getResult().getTransactions())) {
            log.info("No transaction on the block:{}", blockHeight);
            insertBlockAndUpdateConfig(ethBlockInfo, blockHeight, scanBlockConfigEntity);
            return;
        }
        //处理提现
        processWithdrawTransaction(ethBlockInfo);
        //处理上账
        processDepositTransaction(ethBlockInfo);
        insertBlockAndUpdateConfig(ethBlockInfo, blockHeight, scanBlockConfigEntity);
    }

    /**
     * 处理提现
     *
     * @param ethBlockInfo
     * @return
     */
    private void processWithdrawTransaction(EthBlock ethBlockInfo) {
        List<TransactionHistoryEntity> txHistoryList = transactionHistoryService.findStateAndTypeTxHistory(TransactionStateEnum.TRANSACTION_BROADCAST.getCode(), TransactionTypeEnum.TRANSACTION_OUT.getType());
        //获取块上的交易hash
        List<String> txHashList = ethBlockInfo.getResult().getTransactions().stream().map(txHash ->
                txHash.get().toString()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(txHistoryList)) {
            txHistoryList = txHistoryList.stream().filter(txHistory -> txHashList.contains(txHistory.getTransactionHash()))
                    .peek(txHistory -> {
                        //获取交易信息
                        Transaction txInfo = web3jUtil.getTransactionByHash(txHistory.getTransactionHash());
                        //获取交易收据
                        TransactionReceipt txReceipt = web3jUtil.getTransactionReceipt(txHistory.getTransactionHash());
                        txHistory.setBlockHash(txReceipt.getBlockHash());
                        txHistory.setBlockNumber(txReceipt.getBlockNumber());
                        txHistory.setGasLimit(txInfo.getGas());
                        txHistory.setNonce(txInfo.getNonce());
                        txHistory.setState(TransactionStateEnum.TRANSACTION_SUCCESS.getCode());
                        txHistory.setGasPrice(CurrencyMathUtil.weiToEth(String.valueOf(txInfo.getGasPrice())));
                        txHistory.setGasUsed(txReceipt.getGasUsed().toString());
                        txHistory.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
                        log.info("Transaction confirmed,TransactionID:{},TransactionHash:{}", txHistory.getTransactionId(), txHistory.getTransactionHash());
                    }).collect(Collectors.toList());
            List<String> txIdList = txHistoryList.stream().map(TransactionHistoryEntity::getTransactionId).collect(Collectors.toList());
            updateTxOrderList(txIdList);
            transactionHistoryService.batchUpdateTxHistoryList(txHistoryList);
            //解锁from地址并返还手续费
            unlockAddr(txHistoryList);
            //主账户每次打包获得对应交易的手续费需要更新余额
            updateMainAccount();
        }
    }

    /**
     * 更新主账户余额
     */
    private void updateMainAccount() {
        List<AddressEntity> addressList = addressService.findType(AddressTypeEnum.MAIN_ADDR.getCode());
        if (CollectionUtils.isEmpty(addressList)) {
            return;
        }
        AddressEntity addressEntity = addressList.get(0);
        String balance = web3jUtil.getBalance(addressEntity.getWalletAddress());
        if (StringUtils.isEmpty(balance)) {
            log.error("Failed to obtain master account balance");
            return;
        }
        addressService.updateBalance(addressEntity.getWalletAddress(), balance, ZonedDateTime.now(ZoneOffset.UTC));

    }

    /**
     * 更新交易订单
     *
     * @param txIdList
     */
    private void updateTxOrderList(List<String> txIdList) {
        List<TransactionOrderEntity> txOrderList = transactionOrderService.findTxIdList(txIdList);
        if (CollectionUtils.isEmpty(txOrderList)) {
            return;
        }
        txOrderList = txOrderList.stream().peek(txOrder -> txOrder.setState(TransactionOrderStateEnum.SUCCESS.getCode())).collect(Collectors.toList());
        transactionOrderService.batchUpdateTxOrder(txOrderList);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionIds", txOrderList.stream().filter(txOrder -> TransactionOrderTypeEnum.PRIVATE_SYSTEM_TO_PRIVATE_USER.getCode().equals(txOrder.getType())).map(TransactionOrderEntity::getTransactionId).collect(Collectors.toList()));
        jsonObject.put("state", AwardStateEnum.SUCCESS.getCode());
        //更新对应交易的奖励状态
        moneyClient.updateAwardHistoryState(jsonObject);
    }

    /**
     * 处理上账
     *
     * @param ethBlockInfo
     * @return
     */
    private void processDepositTransaction(EthBlock ethBlockInfo) {
        //获取块上的交易hash
        List<String> txHashList = ethBlockInfo.getResult().getTransactions().stream().map(txHash ->
                txHash.get().toString()).collect(Collectors.toList());
        List<TransactionHistoryEntity> txHistoryDepositList = new ArrayList<>();
        for (String txHash : txHashList) {
            //获取交易信息
            Transaction txInfo = web3jUtil.getTransactionByHash(txHash);
            //获取交易收据
            TransactionReceipt txReceipt = web3jUtil.getTransactionReceipt(txHash);
            String toAddr = txInfo.getTo();
            AddressEntity toAddress = addressService.findAddress(toAddr);
            if (null == toAddress) {
                continue;
            }
            //创建上账记录
            TransactionHistoryEntity txHistory = new TransactionHistoryEntity();
            //上账记录没有交易ID
            txHistory.setTransactionId("");
            txHistory.setTransactionHash(txHash);
            txHistory.setTransactionFrom(txInfo.getFrom());
            txHistory.setTransactionTo(txInfo.getTo());
            txHistory.setValue(CurrencyMathUtil.weiToEth(txInfo.getValue().toString()));
            txHistory.setGasPrice(CurrencyMathUtil.weiToEth(txInfo.getGasPrice().toString()));
            txHistory.setGasLimit(txInfo.getGas());
            txHistory.setGasUsed(txReceipt.getGasUsed().toString());
            txHistory.setNonce(txInfo.getNonce());
            txHistory.setTransactionType(TransactionTypeEnum.TRANSACTION_IN.getType());
            txHistory.setState(TransactionStateEnum.TRANSACTION_SUCCESS.getCode());
            txHistory.setCreateTime(ZonedDateTime.now(ZoneOffset.UTC));
            txHistory.setBlockHash(txInfo.getBlockHash());
            txHistory.setBlockNumber(txInfo.getBlockNumber());
            txHistoryDepositList.add(txHistory);
            toAddress.setBalance(CurrencyMathUtil.add(toAddress.getBalance(), CurrencyMathUtil.weiToEth(txInfo.getValue().toString())));
            toAddress.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
            addressService.updateAddressEntity(toAddress);
        }
        transactionHistoryService.batchInsertTxHistoryEntity(txHistoryDepositList);
    }

    /**
     * 添加块信息并修改块缓存表
     *
     * @param ethBlockInfo          块信息
     * @param blockHeight           块高
     * @param scanBlockConfigEntity 上一次扫描的块缓存配置
     */
    private void insertBlockAndUpdateConfig(EthBlock ethBlockInfo, BigInteger blockHeight, ScanBlockConfigEntity scanBlockConfigEntity) {
        BlockInfoEntity blockInfoEntity = new BlockInfoEntity();
        blockInfoEntity.setBlockHash(ethBlockInfo.getResult().getHash());
        blockInfoEntity.setBlockNumber(ethBlockInfo.getResult().getNumber().toString());
        long time = ethBlockInfo.getResult().getTimestamp().longValue();
        //转换linux时间
        blockInfoEntity.setBlockTime(ZonedDateTime.ofInstant(DateUtil.getFormatDate(time).toInstant(),
                ZoneOffset.UTC));
        blockInfoEntity.setCreateTime(ZonedDateTime.now(ZoneOffset.UTC));
        log.info("Adding Block Information,blockHeight:{}", blockHeight);
        blockInfoService.insertBlockInfoEntity(blockInfoEntity);
        ScanBlockConfigEntity scan = new ScanBlockConfigEntity();
        scan.setConfigValue(blockHeight);
        scan.setConfigKey(ScanKeyEnum.SCAN_BLOCK_HEIGHT.getKey());
        if (null == scanBlockConfigEntity) {
            scanBlockConfigService.insert(scan);
        } else {
            scanBlockConfigService.update(scan);
        }

    }

    /**
     * 解锁用户返还手续费
     *
     * @param transactionHistoryList
     */
    private void unlockAddr(List<TransactionHistoryEntity> transactionHistoryList) {
        List<String> addrList = transactionHistoryList.stream().map(TransactionHistoryEntity::getTransactionFrom
        ).collect(Collectors.toList());
        List<AddressEntity> addressEntities = addressService.findAddressList(addrList);
        if (CollectionUtils.isEmpty(addressEntities)) {
            return;
        }
        List<AddressEntity> updateAddressEntities = new ArrayList<>();
        for (AddressEntity addressEntity : addressEntities) {
            for (TransactionHistoryEntity txHistory : transactionHistoryList) {
                if (addressEntity.getWalletAddress().equals(txHistory.getTransactionFrom())) {
                    Long gasLimit = txHistory.getGasLimit().longValue();
                    long unusedGas = gasLimit - Long.valueOf(txHistory.getGasUsed());
                    String paybackFee, balance;
                    //返还手续费
                    if (unusedGas > 0) {
                        paybackFee = CurrencyMathUtil.multiply(txHistory.getGasPrice(), String.valueOf(unusedGas));
                        balance = CurrencyMathUtil.add(addressEntity.getBalance(), paybackFee);
                        addressEntity.setBalance(balance);
                        // moneyClient.updateDta(addressEntity.getWalletAddress(), balance);
                    }
                    //解锁地址
                    addressEntity.setState(AddressStateEnum.UNLOCK.getCode());
                    addressEntity.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
                    if (AddressTypeEnum.SYSTEM_ADDR.getCode().equals(addressEntity.getAddrType())) {
                        SystemAddressUtil.addressQueue.offer(addressEntity);
                    }
                    updateAddressEntities.add(addressEntity);
                }
            }
        }
        addressService.batchUpdateAddressList(updateAddressEntities);
    }
}
