package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.TransactionHistoryEntity;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 下午4:34
 */
public interface ITransactionHistoryService {
    /**
     * 添加交易订单
     * @param txHistoryEntity
     */
    void insertTransactionHistoryEntity(TransactionHistoryEntity txHistoryEntity);

    /**
     * 更新交易订单
     * @param txHistoryEntity
     */
    void updateTransactionHistoryEntity(TransactionHistoryEntity txHistoryEntity);

    /**
     * 根据状态,获取交易订单
     * @param state
     * @return
     */
    List<TransactionHistoryEntity> findState(Integer state);

    /**
     * 批量新增交易订单
     * @param txHistoryEntityList
     * @return
     */
    Integer batchInsertTxHistoryEntity(List<TransactionHistoryEntity> txHistoryEntityList);

    /**
     * 批量更新交易订单
     * @param txHistoryEntityList
     * @return
     */
    Integer batchUpdateTxHistoryList(List<TransactionHistoryEntity> txHistoryEntityList);

    /**
     * 根据交易状态和交易类型获取交易历史
     * @param state
     * @param type
     * @return
     */
    List<TransactionHistoryEntity> findStateAndTypeTxHistory(Integer state,Integer type);

    /**
     * 获取地址转出交易
     *
     * @param addr from地址
     * @param txType 转出 out
     * @return
     */
    List<TransactionHistoryEntity> findAddressTransaction(String addr,Integer txType);

    /**
     * 更加交易ID获取交易订单历史
     * @param transactionId
     * @return
     */
    TransactionHistoryEntity findTransactionHistory(String transactionId);
}
