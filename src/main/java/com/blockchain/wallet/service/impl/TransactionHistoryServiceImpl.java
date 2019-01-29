package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.TransactionHistoryEntity;
import com.blockchain.wallet.mapper.ITransactionHistoryMapper;
import com.blockchain.wallet.service.ITransactionHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 下午6:03
 */
@Service
public class TransactionHistoryServiceImpl implements ITransactionHistoryService {
    @Resource
    private ITransactionHistoryMapper transactionHistoryEntityMapper;

    @Override
    public void insertTransactionHistoryEntity(TransactionHistoryEntity txHistoryEntity) {
        transactionHistoryEntityMapper.insertTransactionHistoryEntity(txHistoryEntity);
    }

    @Override
    public void updateTransactionHistoryEntity(TransactionHistoryEntity txHistoryEntity) {
        transactionHistoryEntityMapper.updateTransactionHistoryEntity(txHistoryEntity);
    }

    @Override
    public List<TransactionHistoryEntity> findState(Integer state) {
        return transactionHistoryEntityMapper.findState(state);
    }

    @Override
    public Integer batchInsertTxHistoryEntity(List<TransactionHistoryEntity> txHistoryEntityList) {
        if(CollectionUtils.isEmpty(txHistoryEntityList)){
            return 0;
        }
        return transactionHistoryEntityMapper.batchInsertTxHistoryEntity(txHistoryEntityList);
    }

    @Override
    public Integer batchUpdateTxHistoryList(List<TransactionHistoryEntity> txHistoryEntityList) {
        if(CollectionUtils.isEmpty(txHistoryEntityList)){
            return 0;
        }
        return transactionHistoryEntityMapper.batchUpdateTxHistoryList(txHistoryEntityList);
    }

    @Override
    public List<TransactionHistoryEntity> findStateAndTypeTxHistory(Integer state, Integer type) {
        return transactionHistoryEntityMapper.findStateAndTypeTxHistory(state,type);
    }

    @Override
    public List<TransactionHistoryEntity> findAddressTransaction(String addr,Integer txType) {
        return transactionHistoryEntityMapper.findAddrTxHistory(addr,txType);
    }

    @Override
    public TransactionHistoryEntity findTransactionHistory(String transactionId) {
        return transactionHistoryEntityMapper.findTransactionHistory(transactionId);
    }

}
