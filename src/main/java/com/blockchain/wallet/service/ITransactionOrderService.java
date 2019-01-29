package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.TransactionOrderEntity;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/18 上午11:41
 */
public interface ITransactionOrderService {
    /**
     * 根据状态获取交易订单
     *
     * @param state
     * @return
     */
    List<TransactionOrderEntity> findState(Integer state);

    /**
     * 根据状态和订单类型获取交易订单
     *
     * @param state 订单状态
     * @param type  订单类型
     * @return
     */
    List<TransactionOrderEntity> findStateAndType(Integer state, Integer type);

    /**
     * 新增交易订单
     *
     * @param txOrder
     */
    void insertTransactionOrder(TransactionOrderEntity txOrder);

    /**
     * 批量新增
     * @param txOrder
     */
    void batchInsertTxOrder(List<TransactionOrderEntity> txOrder);
    /**
     * 根据交易id更新交易订单状态
     * @param transactionId
     * @param state
     */
    void updateTransactionOrderEntity(String transactionId, Integer state);

    /**
     * 批量更新
     *
     * @param txOrderList
     * @return
     */
    Integer batchUpdateTxOrder(List<TransactionOrderEntity> txOrderList);

    /**
     * 根据交易ID列表获取交易订单实体
     *
     * @param txIdList
     * @return
     */
    List<TransactionOrderEntity> findTxIdList(List<String> txIdList);

    /**
     * 根据交易ID获取交易订单实体
     *
     * @param txId
     * @return
     */
    TransactionOrderEntity findTxId(String txId);

    /**
     * 获取from地址指定的订单类型类别
     * @param fromAddress
     * @param type
     * @return
     */
    List<TransactionOrderEntity> findFromAddress(String fromAddress,Integer type);

    /**
     * 更新交易订单
     * @param txOrder
     */
    void updateTransactionOrder(TransactionOrderEntity txOrder);

}
