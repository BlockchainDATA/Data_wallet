package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.entity.TransactionHistoryEntity;
import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.enums.*;
import com.blockchain.wallet.service.IAddressService;
import com.blockchain.wallet.service.IBuildTransactionJobService;
import com.blockchain.wallet.service.ITransactionHistoryService;
import com.blockchain.wallet.service.ITransactionOrderService;
import com.blockchain.wallet.utils.CurrencyMathUtil;
import com.blockchain.wallet.utils.RandomUtil;
import com.blockchain.wallet.utils.UrlConstUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
 * @create 2018/9/17 下午3:52
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BuildTransactionJobServiceImpl implements IBuildTransactionJobService {
    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private IAddressService addressService;
    @Resource
    private ITransactionOrderService transactionOrderService;
    @Resource
    private ITransactionHistoryService transactionHistoryService;
    @Value("${privateEth.gas-limit}")
    private String gasLimit;
    @Value("${privateEth.gas-price}")
    private String gasPrice;

    @Value("${publicEth.gas-limit}")
    private String publicGasLimit;
    @Value("${publicEth.gas-price}")
    private String publicGasPrice;

    @Override
    public void buildTransaction(List<TransactionOrderEntity> txOrderList) {
        //定义交易历史list
        List<TransactionHistoryEntity> txHistoryList = new ArrayList<>();
        //存储更新交易订单list
        List<TransactionOrderEntity> updateTxOrderList = new ArrayList<>();
        //所有提现from地址的实体
        List<AddressEntity> addressList = addressService.findAddressList(txOrderList.stream().map(TransactionOrderEntity::getFromAddr).collect(Collectors.toList()));
        //更新from地址
        List<AddressEntity> updateAddressList = new ArrayList<>();
        AddressEntity fromAddress;
        for (TransactionOrderEntity txOrder : txOrderList) {
            //from地址
            String fromAddr = txOrder.getFromAddr();
            if (StringUtils.isEmpty(fromAddr)) {
                String value = CurrencyMathUtil.add(txOrder.getValue(), txOrder.getFee());
                List<AddressEntity> fromAddrList = addressService.findStateAndTypeAndValue(AddressStateEnum.UNLOCK.getCode(), AddressTypeEnum.SYSTEM_ADDR.getCode(), value);
                if (CollectionUtils.isEmpty(fromAddrList)) {
                    log.info("No corresponding from address was found");
                    continue;
                }
                fromAddress = fromAddrList.get(RandomUtil.getRandomInt(fromAddrList.size()));
            } else {
                fromAddress = addressList.stream().filter(addr -> addr.getWalletAddress().equals(fromAddr)).findFirst().orElse(null);
            }
            if (null == fromAddress) {
                log.info("No corresponding from address was found");
                continue;
            } else if (CurrencyMathUtil.compare(fromAddress.getBalance(), CurrencyMathUtil.add(txOrder.getValue(), txOrder.getFee())) == -1) {
                log.info("Insufficient address balance:{}", fromAddress.getWalletAddress());
                continue;
            } else if (AddressStateEnum.LOCK.getCode().equals(fromAddress.getState())) {
                log.info("Locked address:{}", fromAddress.getWalletAddress());
                continue;
            }
            // 判断是哪一种提现类型
            if (TransactionOrderTypeEnum.PRIVATE_TO_PUBLIC.getCode().equals(txOrder.getType())) {
                continue;
            }
            //获取交易签名
            String transactionSign = getTransactionSign(fromAddress, txOrder.getToAddr(), txOrder.getValue(), txOrder.getFee());
            //获取交易hash
            String txHash = web3jUtil.getTxHash(transactionSign, UrlConstUtil.ETH_NODE_LIST.get(RandomUtil.getRandomInt(UrlConstUtil.ETH_NODE_LIST.size())));
            TransactionHistoryEntity txHistory;
            if (StringUtils.isEmpty(txHash)) {
                //交易失败获取最新nonce和余额
                BigInteger nonce = web3jUtil.getNonce(fromAddress.getWalletAddress(), UrlConstUtil.ETH_NODE_LIST.get(RandomUtil.getRandomInt(UrlConstUtil.ETH_NODE_LIST.size())));
                fromAddress.setNonce(nonce);
                updateAddressList.add(fromAddress);
                //交易失败
//                txOrder.setState(TransactionOrderStateEnum.FAIL.getCode());
//                txHistory = getTxHistory(txOrder, transactionSign, txHash, fromAddress.getNonce(), TransactionOrderStateEnum.FAIL);
//                updateTxOrderList.add(txOrder);
//                txHistoryList.add(txHistory);
//                log.info("获取txHash失败,交易失败,订单id{}", txOrder.getTransactionId());
                continue;
            }
            txHistory = getTxHistory(txOrder, transactionSign, txHash, fromAddress.getNonce(), TransactionOrderStateEnum.HANDLING);
            log.info("User withdrawal, transaction ID:{}, Transaction signature:{},transactionHash:{}", txHistory.getTransactionId(), transactionSign, txHash);
            txHistory.setTransactionFrom(fromAddress.getWalletAddress());
            txHistoryList.add(txHistory);
            updateAddressList.add(getAddressEntity(fromAddress, txOrder));
            txOrder.setState(TransactionOrderStateEnum.HANDLING.getCode());
            updateTxOrderList.add(txOrder);
        }
        addressService.batchUpdateAddressList(updateAddressList);
        transactionOrderService.batchUpdateTxOrder(updateTxOrderList);
        transactionHistoryService.batchInsertTxHistoryEntity(txHistoryList);
    }

    /**
     * 更新from地址
     *
     * @param addressEntity from地址实体
     * @param txOrder       交易订单
     */
    private AddressEntity getAddressEntity(AddressEntity addressEntity, TransactionOrderEntity txOrder) {
        String balance = CurrencyMathUtil.subtract(addressEntity.getBalance(), CurrencyMathUtil.add(txOrder.getValue(), txOrder.getFee()));
        addressEntity.setNonce(addressEntity.getNonce().add(BigInteger.valueOf(1)));
        addressEntity.setState(AddressStateEnum.LOCK.getCode());
        addressEntity.setBalance(balance);
        addressEntity.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
        return addressEntity;
    }

    /**
     * 获取交易签名
     *
     * @param fromAddressEntity from 地址实体
     * @param toAddr            to地址
     * @param value             金额
     * @param fee               手续费
     */
    private String getTransactionSign(AddressEntity fromAddressEntity, String toAddr, String value, String fee) {
        String gasPrice = CurrencyMathUtil.divide(fee, this.gasLimit);
        return web3jUtil.getETHTransactionSign(fromAddressEntity.getPrivateKey(), fromAddressEntity.getNonce(), toAddr
                , gasPrice, new BigInteger(this.gasLimit), value);
    }

    /**
     * 构建交易订单历史
     *
     * @param txOrder 交易订单
     * @param sign    交易签名
     * @param txHash  交易hash
     * @param nonce   地址的nonce
     * @return
     */
    private TransactionHistoryEntity getTxHistory(TransactionOrderEntity txOrder,
                                                  String sign, String txHash,
                                                  BigInteger nonce,
                                                  TransactionOrderStateEnum stateEnum) {
        String gasPrice = CurrencyMathUtil.divide(txOrder.getFee(), this.gasLimit);
        TransactionHistoryEntity txHistory = new TransactionHistoryEntity();
        txHistory.setTransactionId(txOrder.getTransactionId());
        txHistory.setTransactionFrom(txOrder.getFromAddr());
        txHistory.setTransactionTo(txOrder.getToAddr());
        if (TransactionOrderTypeEnum.PRIVATE_TO_PUBLIC.getCode().equals(txOrder.getType())) {
            txHistory.setTransactionType(TransactionTypeEnum.PRIVATE_TO_PUBLIC.getType());
        } else {
            txHistory.setTransactionType(TransactionTypeEnum.TRANSACTION_OUT.getType());
        }
        txHistory.setGasLimit(new BigInteger(this.gasLimit));
        txHistory.setGasPrice(gasPrice);
        txHistory.setValue(txOrder.getValue());
        txHistory.setCreateTime(ZonedDateTime.now(ZoneOffset.UTC));
        txHistory.setNonce(nonce);
        txHistory.setTransactionHash(txHash);
        txHistory.setSign(sign);
        if (TransactionOrderStateEnum.FAIL.getCode().equals(stateEnum.getCode())) {
            txHistory.setState(TransactionStateEnum.TRANSACTION_FAIL.getCode());
        } else {
            txHistory.setState(TransactionStateEnum.TRANSACTION_BROADCAST.getCode());
        }
        return txHistory;
    }
}
