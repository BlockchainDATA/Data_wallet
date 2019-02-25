package com.blockchain.wallet.handler;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.client.MoneyClient;
import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.entity.TransactionHistoryEntity;
import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.enums.*;
import com.blockchain.wallet.service.IAddressService;
import com.blockchain.wallet.service.ITransactionHistoryService;
import com.blockchain.wallet.service.ITransactionOrderService;
import com.blockchain.wallet.utils.CurrencyMathUtil;
import com.blockchain.wallet.utils.RandomUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/17 4:48 PM
 */
@Slf4j
public class BuildTransactionHandler implements Runnable {
    private IAddressService addressService;
    private Web3jUtil web3jUtil;
    private ITransactionOrderService transactionOrderService;
    private ITransactionHistoryService transactionHistoryService;
    private TransactionOrderEntity txOrder;
    private List<String> ethNodeList;

    private String gasLimit;
    private AddressEntity fromAddress;
    private MoneyClient moneyClient;

    public BuildTransactionHandler(IAddressService addressService, Web3jUtil web3jUtil, ITransactionOrderService transactionOrderService, ITransactionHistoryService transactionHistoryService, TransactionOrderEntity txOrder, List<String> ethNodeList, String gasLimit, AddressEntity fromAddress, MoneyClient moneyClient) {
        this.addressService = addressService;
        this.web3jUtil = web3jUtil;
        this.transactionOrderService = transactionOrderService;
        this.transactionHistoryService = transactionHistoryService;
        this.txOrder = txOrder;
        this.ethNodeList = ethNodeList;
        this.gasLimit = gasLimit;
        this.fromAddress = fromAddress;
        this.moneyClient = moneyClient;
    }


    @Override
    public void run() {

        String fromAddr = txOrder.getFromAddr();

        if (!StringUtils.isEmpty(fromAddr)) {
            fromAddress = addressService.findAddrAndState(fromAddr, AddressStateEnum.UNLOCK.getCode());
        }
        if (null == fromAddress) {
            log.info("No corresponding from address was found");
            return;
        }
        if (CurrencyMathUtil.compare(fromAddress.getBalance(), CurrencyMathUtil.add(txOrder.getValue(), txOrder.getFee())) == -1) {
            log.info("Insufficient address balance:{}", fromAddress.getWalletAddress());
            return;
        } else if (AddressStateEnum.LOCK.getCode().equals(fromAddress.getState())) {
            log.info("Locked address:{}", fromAddress.getWalletAddress());
            return;
        }
        // 判断是哪一种提现类型
        if (TransactionOrderTypeEnum.PRIVATE_TO_PUBLIC.getCode().equals(txOrder.getType())) {
            return;
        }
        //获取交易签名
        String transactionSign = getTransactionSign(fromAddress, txOrder.getToAddr(), txOrder.getValue(), txOrder.getFee());
        //获取交易hash
        String txHash = web3jUtil.getTxHash(fromAddress.getWalletAddress(), transactionSign);
        log.info("User withdrawal, transaction ID:{}, Transaction signature:{},transactionHash:{}", txOrder.getTransactionId(), transactionSign, txHash);
        TransactionHistoryEntity txHistory;
        Transaction transactionByHash = web3jUtil.getTransactionByHash(txHash);
        log.info("txHash:{},transactionByHash Details:" + txHash, transactionByHash);
        if (Objects.isNull(transactionByHash)) {
            //交易失败获取最新nonce和余额
            BigInteger nonce = web3jUtil.getNonce(fromAddress.getWalletAddress());
            String balance = web3jUtil.getBalance(fromAddress.getWalletAddress());
            fromAddress.setNonce(nonce);
            fromAddress.setBalance(balance);
            addressService.updateAddressEntity(fromAddress);
            if (txOrder.getRetry() >= 100) {
                txOrder.setState(TransactionOrderStateEnum.FAIL.getCode());
                txHistory = getTxHistory(txOrder, transactionSign, txHash, fromAddress.getNonce(), TransactionOrderStateEnum.FAIL);
                transactionOrderService.updateTransactionOrder(txOrder);
                transactionHistoryService.insertTransactionHistoryEntity(txHistory);
                JSONObject jsonObject = new JSONObject();
                List<String> list = new ArrayList<>();
                list.add(txOrder.getTransactionId());
                jsonObject.put("transactionIds", list);
                jsonObject.put("state", AwardStateEnum.WALLET_BUILD.getCode());
                moneyClient.updateAwardHistoryState(jsonObject);
                log.info("Failure to get txHash, transaction failure, order id{}", txOrder.getTransactionId());
                return;
            } else {
                txOrder.setRetry(txOrder.getRetry() + 1);
                txOrder.setFromAddr(fromAddr);
                transactionOrderService.updateTransactionOrder(txOrder);
                log.info("The transaction starts retrying, the transaction ID: {}, the number of retries:{}", txOrder.getTransactionId(), txOrder.getRetry());
                return;
            }
        }
        txHistory = getTxHistory(txOrder, transactionSign, txHash, fromAddress.getNonce(), TransactionOrderStateEnum.HANDLING);
        txHistory.setTransactionFrom(fromAddress.getWalletAddress());
        txOrder.setState(TransactionOrderStateEnum.HANDLING.getCode());
        addressService.updateAddressEntity(getAddressEntity(fromAddress, txOrder));
        transactionOrderService.updateTransactionOrder(txOrder);
        transactionHistoryService.insertTransactionHistoryEntity(txHistory);
//        rlock.unlock();
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
}
