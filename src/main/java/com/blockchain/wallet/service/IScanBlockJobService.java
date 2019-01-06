package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.ScanBlockConfigEntity;

import java.math.BigInteger;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 下午1:26
 */
public interface IScanBlockJobService {
    /**
     * 扫块的服务
     *
     * @param blockHeight
     */
    /**
     * 扫块服务
     * @param blockHeight 当前扫描的块高
     * @param scanBlockConfigEntity 上一次扫描的块信息
     */
    void scanBlock(BigInteger blockHeight, ScanBlockConfigEntity scanBlockConfigEntity);
}
