package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.ScanBlockConfigEntity;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 上午11:00
 */
public interface IScanBlockConfigService {
    /**
     * 获取缓存值
     * @param key
     * @return
     */
    ScanBlockConfigEntity getScanBlockConfig(String key);

    /**
     * 插入缓存
     * @param entity 待插入的缓存信息
     * @return 更新的行数
     */
    Integer insert(ScanBlockConfigEntity entity);

    /**
     * 更新缓存
     * @param entity 待更新的缓存信息
     * @return 更新的行数
     */
    Integer update(ScanBlockConfigEntity entity);
}
