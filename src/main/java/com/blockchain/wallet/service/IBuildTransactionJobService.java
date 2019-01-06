package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.TransactionOrderEntity;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/17 下午3:52
 */
public interface IBuildTransactionJobService {
    /**
     * 构建私有链的交易
     * @param txOrderList 交易列表
     */
    void buildTransaction(List<TransactionOrderEntity> txOrderList);
}
