package com.blockchain.wallet.service;


import com.blockchain.wallet.entity.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 上午11:07
 */
public interface IWalletService {
    /**
     * 创建钱包地址
     * @param type
     * @return 地址
     */
    String createAddr(Integer type);

    /**
     * 根据用户地址查询用户信息
     *
     * @param addr 地址
     * @return
     */
    AddressEntity queryAddressEntity(String addr);

    /**
     * 构建交易
     *
     * @param txParam 交易订单
     * @return 交易订单的id
     */
    String buildTransaction(TransactionParamEntity txParam);

//    /**
//     * 提现交易(用户发起提现获取以太坊公链的DTA)
//     * @param txParam 交易订单
//     * @return
//     */
//    String withdrawTransaction(TransactionParamEntity txParam);

    /**
     * 获取地址转出交易
     *
     * @param addr 地址
     * @return
     */
    List<TransactionHistoryEntity> findAddressOutTransaction(String addr);

    /**
     * 获取地址转入交易
     *
     * @param addr 地址
     * @return
     */
    List<TransactionHistoryEntity> findAddressInTransaction(String addr);

    /**
     * 根据状态和订单类型获取交易订单
     *
     * @param state 订单状态
     * @param type  订单类型
     * @return
     */
    List<TransactionOrderEntity> getTransactionOrderList(Integer state, Integer type);

    /**
     * 处理体现到公有链的请求
     *
     * @param transactionId 交易订单ID
     * @param state         状态
     * @return
     */
    String handlerPublicWithdrawOrder(String transactionId, Integer state);

    /**
     * 获取地址的体现订单列表
     *
     * @param fromAddress
     * @return
     */
    List<TransactionOrderEntity> getAddressWithdrawOrderList(String fromAddress);

    /**
     * 获取提现订单详情
     *
     * @param transactionId
     * @return
     */
    WithdrawPublicEntity getWithdrawDetail(String transactionId);

    /**
     * 通过交易ID获取私有链的交易hash
     *
     * @param transactionId 交易ID
     * @return
     */
    TransactionHistoryEntity getPrivateChainTransaction(String transactionId);


    /**
     * 使用私钥转账
     *
     * @param fromAddress from地址
     * @param toAddress   to地址
     * @param privateKey  私钥
     * @param value       金额
     * @return 交易Hash
     */
    String privateKeyTransfer(String fromAddress, String toAddress, String privateKey, String value);

    /**
     * 获取扫块的配置
     * @return
     */
    Map<String,BigInteger> getScanBlockConfig();

}
