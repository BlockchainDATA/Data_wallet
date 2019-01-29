package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.TransactionParamEntity;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/14 2:47 PM
 */
public interface IBuildTransactionOrderJobService {

    /**
     * 构建交易订单
     * @param dtaTransactionList
     */
    void buildTransactionOrder(List<TransactionParamEntity> dtaTransactionList);
}
