package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.mapper.ITransactionOrderMapper;
import com.blockchain.wallet.service.ITransactionOrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/18 上午11:49
 */
@Service
public class TransactionOrderServiceImpl implements ITransactionOrderService {
    @Resource
    private ITransactionOrderMapper transactionOrderMapper;

    @Override
    public List<TransactionOrderEntity> findState(Integer state) {
        return transactionOrderMapper.findState(state);
    }

    @Override
    public List<TransactionOrderEntity> findStateAndType(Integer state, Integer type) {
        return transactionOrderMapper.findStateAndType(state, type);
    }

    @Override
    public void insertTransactionOrder(TransactionOrderEntity txOrder) {
        transactionOrderMapper.insertTransactionOrder(txOrder);
    }

    @Override
    public void batchInsertTxOrder(List<TransactionOrderEntity> txOrder) {
        if (!CollectionUtils.isEmpty(txOrder)) {
            transactionOrderMapper.batchInsertTxOrder(txOrder);
        }
    }

    @Override
    public void updateTransactionOrderEntity(String transactionId, Integer state) {
        transactionOrderMapper.updateTransactionOrderEntity(transactionId, state, ZonedDateTime.now(ZoneOffset.UTC));
    }


    @Override
    public Integer batchUpdateTxOrder(List<TransactionOrderEntity> txOrderList) {
        if (CollectionUtils.isEmpty(txOrderList)) {
            return 0;
        }
        return transactionOrderMapper.batchUpdateTxOrder(txOrderList);
    }

    @Override
    public List<TransactionOrderEntity> findTxIdList(List<String> txIdList) {
        if (CollectionUtils.isEmpty(txIdList)) {
            return null;
        }
        return transactionOrderMapper.findTxIdList(txIdList);
    }

    @Override
    public TransactionOrderEntity findTxId(String txId) {
        return transactionOrderMapper.findTxId(txId);
    }

    @Override
    public List<TransactionOrderEntity> findFromAddress(String fromAddress, Integer type) {
        return transactionOrderMapper.findFromAddress(fromAddress, type);
    }

    @Override
    public void updateTransactionOrder(TransactionOrderEntity txOrder) {
        transactionOrderMapper.updateTransactionOrder(txOrder);
    }
}
