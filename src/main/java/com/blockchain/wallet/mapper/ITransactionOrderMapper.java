package com.blockchain.wallet.mapper;

import com.blockchain.wallet.entity.TransactionOrderEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/18 下午1:24
 */
@Repository
public interface ITransactionOrderMapper {
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
    List<TransactionOrderEntity> findStateAndType(@Param("state") Integer state, @Param("type") Integer type);

    /**
     * 新增交易订单
     *
     * @param txOrder
     */
    void insertTransactionOrder(TransactionOrderEntity txOrder);

    /**
     * 根据交易id更新交易订单状态
     *
     * @param transactionId
     * @param state
     */
    void updateTransactionOrderEntity(@Param("transactionId") String transactionId, @Param("state") Integer state, @Param("dateTime") ZonedDateTime dateTime);

    /**
     * 批量更新
     *
     * @param txOrderList
     * @return
     */
    Integer batchUpdateTxOrder(List<TransactionOrderEntity> txOrderList);

    /**
     * 根据交易ID获取交易订单实体
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
     * 批量新增
     *
     * @param txOrder
     */
    void batchInsertTxOrder(List<TransactionOrderEntity> txOrder);

    /**
     * 获取from地址指定的订单类型类别
     *
     * @param fromAddress
     * @param type
     * @return
     */
    List<TransactionOrderEntity> findFromAddress(@Param("fromAddress") String fromAddress, @Param("type") Integer type);
}
