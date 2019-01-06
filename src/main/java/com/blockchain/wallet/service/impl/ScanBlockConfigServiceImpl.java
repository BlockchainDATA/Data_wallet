package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.ScanBlockConfigEntity;
import com.blockchain.wallet.mapper.IScanBlockConfigMapper;
import com.blockchain.wallet.service.IScanBlockConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 上午11:03
 */
@Service
public class ScanBlockConfigServiceImpl implements IScanBlockConfigService {

    @Resource
    private IScanBlockConfigMapper scanBlockConfigMapper;

    @Override
    public ScanBlockConfigEntity getScanBlockConfig(String key) {
        return scanBlockConfigMapper.getScanBlockConfig(key);
    }

    @Override
    public Integer insert(ScanBlockConfigEntity entity) {
        return scanBlockConfigMapper.insert(entity);
    }

    @Override
    public Integer update(ScanBlockConfigEntity entity) {
        return scanBlockConfigMapper.update(entity);
    }
}
