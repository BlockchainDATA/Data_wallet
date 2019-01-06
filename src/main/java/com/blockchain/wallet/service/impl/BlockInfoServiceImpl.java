package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.BlockInfoEntity;
import com.blockchain.wallet.mapper.IBlockInfoMapper;
import com.blockchain.wallet.service.IBlockInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/19 下午4:43
 */
@Service
public class BlockInfoServiceImpl implements IBlockInfoService {
    @Resource
    private IBlockInfoMapper blockInfoMapper;
    @Override
    public void insertBlockInfoEntity(BlockInfoEntity blockInfo) {
        blockInfoMapper.insertBlockInfoEntity(blockInfo);
    }

    @Override
    public BlockInfoEntity findByBlockNumber(BigInteger blockNumber) {
        return blockInfoMapper.findByBlockNumber(blockNumber);
    }

}
