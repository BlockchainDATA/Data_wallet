package com.blockchain.wallet.mapper;

import com.blockchain.wallet.entity.TransactionHistoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 下午2:03
 */
@Repository
public interface ITransactionHistoryMapper {

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
     * 根据状态获取交易订单
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
    List<TransactionHistoryEntity> findStateAndTypeTxHistory(@Param("state") Integer state, @Param("type") Integer type);

    /**
     * 获取地址转出交易
     *
     * @param addr 交易from地址
     * @param txType 交易类型 转出out
     * @return
     */
    List<TransactionHistoryEntity> findAddrTxHistory(@Param("addr") String addr,@Param("txType") Integer txType);


    /**
     * 更加交易ID获取交易订单历史
     * @param transactionId
     * @return
     */
    TransactionHistoryEntity findTransactionHistory(String transactionId);

}
