package com.blockchain.wallet.entity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 块信息实体
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/12 上午10:48
 */
public class BlockInfoEntity {
    /**
     * 块高
     */
    private String blockNumber;
    /**
     * 块hash
     */
    private String blockHash;
    /**
     * 块时间
     */
    private ZonedDateTime blockTime;
    /**
     * 创建时间
     */
    private ZonedDateTime createTime = ZonedDateTime.now(ZoneOffset.UTC);

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public ZonedDateTime getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(ZonedDateTime blockTime) {
        this.blockTime = blockTime;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BlockInfoEntity{" +
                "blockNumber='" + blockNumber + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", blockTime=" + blockTime +
                ", createTime=" + createTime +
                '}';
    }
}
