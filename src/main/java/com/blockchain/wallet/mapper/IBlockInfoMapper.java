package com.blockchain.wallet.mapper;

import com.blockchain.wallet.entity.BlockInfoEntity;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/19 下午4:43
 */
@Repository
public interface IBlockInfoMapper {
    /**
     * 新增块信息
     * @param blockInfo
     */
    void insertBlockInfoEntity(BlockInfoEntity blockInfo);
    /**
     * 根据blockNumber获取块信息
     * @param blockNumber
     * @return
     */
    BlockInfoEntity findByBlockNumber(BigInteger blockNumber);
}
