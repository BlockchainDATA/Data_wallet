package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.WithdrawPublicEntity;
import com.blockchain.wallet.mapper.IWithdrawPublicMapper;
import com.blockchain.wallet.service.IWithdrawPublicService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 5:30 PM
 */
@Service
public class WithdrawPublicServiceImpl implements IWithdrawPublicService {
    @Resource
    private IWithdrawPublicMapper withdrawPublicMapper;

    @Override
    public Integer batchInsertWithdrawPublic(List<WithdrawPublicEntity> withdrawPublicEntities) {
        if (CollectionUtils.isEmpty(withdrawPublicEntities)) {
            return 0;
        }
        return withdrawPublicMapper.batchInsertWithdrawPublic(withdrawPublicEntities);
    }

    @Override
    public void batchUpdateWithdrawPublic(List<WithdrawPublicEntity> withdrawPublicEntities) {
        if (!CollectionUtils.isEmpty(withdrawPublicEntities)) {
            withdrawPublicMapper.batchUpdateWithdrawPublic(withdrawPublicEntities);
        }
    }

    @Override
    public Integer insertWithdrawPublic(WithdrawPublicEntity withdrawPublicEntity) {
        return withdrawPublicMapper.insertWithdrawPublic(withdrawPublicEntity);
    }

    @Override
    public List<WithdrawPublicEntity> findState(Integer state) {
        return withdrawPublicMapper.findState(state);
    }

    @Override
    public WithdrawPublicEntity findWithdrawDetail(String transactionId) {
        return withdrawPublicMapper.findWithdrawDetail(transactionId);
    }
}
