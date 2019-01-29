package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.entity.AddressPublicEntity;
import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.entity.WithdrawPublicEntity;
import com.blockchain.wallet.enums.*;
import com.blockchain.wallet.service.*;
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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/26 5:47 PM
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BuildPublicChainTransferJobServiceImpl implements IBuildPublicChainTransferJobService {
    @Resource
    private IWithdrawPublicService withdrawPublicService;
    @Resource
    private IAddressPublicService addressPublicService;
    @Resource
    private ITransactionOrderService transactionOrderService;
    @Resource
    private IAddressService addressService;
    @Resource
    private Web3jUtil web3jUtil;
    @Value("${privateEth.gas-limit}")
    private String privateGasLimit;
    @Value("${privateEth.gas-price}")
    private String privateGasPrice;

    @Value("${publicEth.gas-limit}")
    private String publicGasLimit;
    @Value("${publicEth.gas-price}")
    private String publicGasPrice;
    @Value("${erc.accuracy}")
    private Integer accuracy;
    @Value("${publicEth.contractAddress}")
    private String contractAddress;


    @Override
    public void buildPublicChainTransfer() {
        List<WithdrawPublicEntity> withdrawPublicList = withdrawPublicService.findState(TransactionStateEnum.TRANSACTION_UNTREATED.getCode());
        List<String> fromAddrList = withdrawPublicList.stream().map(WithdrawPublicEntity::getPublicFrom).collect(Collectors.toList());
        List<AddressPublicEntity> addressPublicList = addressPublicService.findAddress(fromAddrList);
        List<TransactionOrderEntity> txBuildOrderList = transactionOrderService.findTxIdList(withdrawPublicList.stream().map(WithdrawPublicEntity::getPrivateTransactionId).collect(Collectors.toList()));
        List<TransactionOrderEntity> txOrderList = transactionOrderService.findTxIdList(withdrawPublicList.stream().map(WithdrawPublicEntity::getTransactionId).collect(Collectors.toList()));
        List<WithdrawPublicEntity> updateWithdrawPublicList = new ArrayList<>();
        List<TransactionOrderEntity> updatePublicTxOrderList = new ArrayList<>();
        List<AddressPublicEntity> updateAddressPublic = new ArrayList<>();
        for (WithdrawPublicEntity withdrawPublic : withdrawPublicList) {
            //获取对应构建的私有链交易记录
            TransactionOrderEntity transactionOrderEntity = txBuildOrderList.stream().filter(txOrder ->
                    txOrder.getTransactionId().equals(withdrawPublic.getPrivateTransactionId()) && txOrder.getState().equals(TransactionOrderStateEnum.SUCCESS.getCode()))
                    .findFirst().orElse(null);
            if (null == transactionOrderEntity) {
                log.info("Private Chain Transactions Unhandled Successfully,privateTransactionId:{}", withdrawPublic.getPrivateTransactionId());
                continue;
            }
            //获取提现到公有链的交易ID
            TransactionOrderEntity publicTxOrder = txOrderList.stream().filter(txOrder -> txOrder.getTransactionId().equals(withdrawPublic.getTransactionId())).findFirst().orElse(null);
            if (null == publicTxOrder) {
                log.info("Transaction orders do not exist,transactionId:{}", withdrawPublic.getTransactionId());
                continue;
            }
            AddressPublicEntity addressPublicEntity = addressPublicList.stream().filter(address -> address.getWalletAddress().equals(withdrawPublic.getPublicFrom())).findFirst().orElse(null);
            if (null == addressPublicEntity) {
                log.info("ETH public chain address does not exist");
                continue;
            }
            //ERC20的转账获取交易签名
            String ercTransactionSign = web3jUtil.getERCTransactionSign(addressPublicEntity.getPrivateKey(), addressPublicEntity.getNonce(), contractAddress, withdrawPublic.getPublicTo(), publicGasPrice, new BigInteger(publicGasLimit), new BigInteger(CurrencyMathUtil.multiply(withdrawPublic.getValue(), String.valueOf(Math.pow(10, accuracy)))));
            if (StringUtils.isEmpty(ercTransactionSign)) {
                log.error("ERC20 Failure to obtain transaction signature");
                withdrawPublic.setState(TransactionStateEnum.TRANSACTION_FAIL.getCode());
                publicTxOrder.setState(TransactionOrderStateEnum.FAIL.getCode());
            }
            //广播交易获取交易hash
            String txHash = web3jUtil.getTxHash(addressPublicEntity.getWalletAddress(), ercTransactionSign, UrlConstUtil.ETH_PUBLIC_NODE_URL);
            if (StringUtils.isEmpty(txHash)) {
                log.error("ERC20 Failed to get txHash");
                //保证from地址下次nonce地址不会出错
                BigInteger nonce = web3jUtil.getNonce(addressPublicEntity.getWalletAddress(), UrlConstUtil.ETH_PUBLIC_NODE_URL);
                String balance = web3jUtil.getBalance(addressPublicEntity.getWalletAddress(), UrlConstUtil.ETH_PUBLIC_NODE_URL);
                String publicFee = CurrencyMathUtil.multiply(publicGasLimit, publicGasPrice);
                if (CurrencyMathUtil.compare(balance, publicFee) == -1) {
                    log.info("Public chain address ETH is insufficient to pay handling fee, please recharge,address:{}", addressPublicEntity.getWalletAddress());
                }
                String tokenBalance = web3jUtil.getTokenBalance(addressPublicEntity.getWalletAddress());
                if (CurrencyMathUtil.compare(tokenBalance, withdrawPublic.getValue()) == -1) {
                    log.info("Public chain address DTA is insufficient to support transfer,address:{}", addressPublicEntity.getWalletAddress());
                }
                addressPublicEntity.setNonce(nonce);
                addressPublicEntity.setEthBalance(balance);
                addressPublicEntity.setDtaBalance(tokenBalance);
                addressPublicEntity.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
                updateAddressPublic.add(addressPublicEntity);
                continue;
            }
            publicTxOrder.setState(TransactionOrderStateEnum.SUCCESS.getCode());
            withdrawPublic.setNonce(addressPublicEntity.getNonce().subtract(BigInteger.valueOf(1L)));
            withdrawPublic.setState(TransactionStateEnum.TRANSACTION_BROADCAST.getCode());
            withdrawPublic.setPublicTransactionHash(txHash);
            withdrawPublic.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
            updateWithdrawPublicList.add(withdrawPublic);
            updatePublicTxOrderList.add(publicTxOrder);
        }
        addressPublicService.batchUpdateAddressPublic(updateAddressPublic);
        withdrawPublicService.batchUpdateWithdrawPublic(updateWithdrawPublicList);
        transactionOrderService.batchUpdateTxOrder(updatePublicTxOrderList);
        List<TransactionOrderEntity> transactionOrderList = transactionOrderService.findStateAndType(TransactionOrderStateEnum.PASS.getCode(), TransactionOrderTypeEnum.PRIVATE_TO_PUBLIC.getCode());
        List<TransactionOrderEntity> updateTransactionOrderList = new ArrayList<>();
        List<TransactionOrderEntity> buildTxOrderList = new ArrayList<>();
        List<WithdrawPublicEntity> buildWithdrawPublicList = new ArrayList<>();
        List<AddressEntity> systemPrivateAddrList = addressService.findType(AddressTypeEnum.SYSTEM_ADDR.getCode());
        AddressEntity toPrivateAddress;
        for (TransactionOrderEntity transactionOrder : transactionOrderList) {
            if (CollectionUtils.isEmpty(systemPrivateAddrList)) {
                log.info("No eligible private chain system address, transaction ID:{}", transactionOrder.getTransactionId());
                continue;
            }
            toPrivateAddress = systemPrivateAddrList.get(RandomUtil.getRandomInt(systemPrivateAddrList.size()));
            TransactionOrderEntity buildTxOrder = getTransactionOrder(transactionOrder.getFromAddr(), toPrivateAddress.getWalletAddress(), transactionOrder.getValue(), transactionOrder.getFee());
            WithdrawPublicEntity withdrawPublic = getWithdrawPublic(transactionOrder, buildTxOrder.getTransactionId(), toPrivateAddress.getWalletAddress());
            if (null == withdrawPublic) {
                continue;
            }
            buildWithdrawPublicList.add(withdrawPublic);
            buildTxOrderList.add(buildTxOrder);
            transactionOrder.setState(TransactionOrderStateEnum.HANDLING.getCode());
            updateTransactionOrderList.add(transactionOrder);
        }
        withdrawPublicService.batchInsertWithdrawPublic(buildWithdrawPublicList);
        transactionOrderService.batchInsertTxOrder(buildTxOrderList);
        transactionOrderService.batchUpdateTxOrder(updateTransactionOrderList);

    }

    /**
     * 构建提现到公有链的交易订单历史
     *
     * @param txOrder 交易订单
     * @return
     */
    private WithdrawPublicEntity getWithdrawPublic(TransactionOrderEntity txOrder, String privateTransactionId, String privateToAddress) {
        WithdrawPublicEntity withdrawPublicEntity = new WithdrawPublicEntity();
        String publicFee = CurrencyMathUtil.multiply(publicGasLimit, publicGasPrice);
        //获取公有链的from地址
        List<AddressPublicEntity> publicFroms = addressPublicService.findStateAndTypeAndValue(AddressStateEnum.UNLOCK.getCode(), AddressTypeEnum.SYSTEM_ADDR.getCode(), publicFee, txOrder.getValue());
        if (CollectionUtils.isEmpty(publicFroms)) {
            log.info("No suitable system address was found for the ETH public chain, DAT amount{},fee:{}", txOrder.getValue(), publicFee);
            return null;
        }
        AddressPublicEntity addressPublicFrom = publicFroms.get(RandomUtil.getRandomInt(publicFroms.size()));
        //锁定账户
        addressPublicFrom.setState(AddressStateEnum.LOCK.getCode());
        BigInteger nonce = addressPublicFrom.getNonce();
        //nonce加1
        addressPublicFrom.setNonce(nonce.add(BigInteger.valueOf(1)));
        addressPublicFrom.setDtaBalance(CurrencyMathUtil.subtract(addressPublicFrom.getDtaBalance(), txOrder.getValue()));
        //减去手续费
        addressPublicFrom.setEthBalance(CurrencyMathUtil.subtract(addressPublicFrom.getEthBalance(), publicFee));
        addressPublicFrom.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
        //更新公有链的地址信息
        addressPublicService.updateAddressPublic(addressPublicFrom);
        withdrawPublicEntity.setTransactionId(txOrder.getTransactionId());
        withdrawPublicEntity.setPrivateTransactionId(privateTransactionId);
        withdrawPublicEntity.setPublicFrom(addressPublicFrom.getWalletAddress());
        withdrawPublicEntity.setPublicTo(txOrder.getToAddr());
        withdrawPublicEntity.setPrivateFrom(txOrder.getFromAddr());
        withdrawPublicEntity.setPrivateTo(privateToAddress);
        withdrawPublicEntity.setNonce(nonce);
        withdrawPublicEntity.setGasLimit(new BigInteger(publicGasLimit));
        withdrawPublicEntity.setMemo(txOrder.getMemo());
        //公有链手续费由我们自己出
        withdrawPublicEntity.setValue(CurrencyMathUtil.subtract(txOrder.getValue(), txOrder.getFee()));
        withdrawPublicEntity.setState(TransactionStateEnum.TRANSACTION_UNTREATED.getCode());
        return withdrawPublicEntity;
    }

    /**
     * 构建私有链交易订单
     *
     * @param fromAddr
     * @param toAddr
     * @param value
     * @param fee
     * @return
     */
    private TransactionOrderEntity getTransactionOrder(String fromAddr, String toAddr, String value, String fee) {
        TransactionOrderEntity transactionOrder = new TransactionOrderEntity();
        transactionOrder.setTransactionId(UUID.randomUUID().toString().replace("-", ""));
        transactionOrder.setFromAddr(fromAddr);
        transactionOrder.setToAddr(toAddr);
        transactionOrder.setValue(value);
        transactionOrder.setFee(fee);
        transactionOrder.setState(TransactionOrderStateEnum.INIT.getCode());
        transactionOrder.setType(TransactionOrderTypeEnum.SYSTEM_BUILD_PRIVATE_TO_PRIVATE.getCode());
        return transactionOrder;
    }

}
