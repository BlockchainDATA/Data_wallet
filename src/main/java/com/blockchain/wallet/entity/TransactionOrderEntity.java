package com.blockchain.wallet.entity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/18 上午11:37
 */
public class TransactionOrderEntity {

    private Long id;
    /**
     * 交易ID
     */
    private String transactionId;
    /**
     * from 地址
     */
    private String fromAddr;
    /**
     * to 地址
     */
    private String toAddr;
    /**
     * 金额
     */
    private String value;
    /**
     * 手续费
     */
    private String fee;
    /**
     * 状态 0:初始1:处理中 2:成功 3:失败 4:拒绝 5:审核通过
     */
    private Integer state;
    /**
     * 创建时间
     */
    private ZonedDateTime createTime = ZonedDateTime.now(ZoneOffset.UTC);
    /**
     * 更新时间
     */
    private ZonedDateTime updateTime;

    /**
     * 提现订单类型1:表示用户a->用户b双方都是私有链操作,2:表示地址a(私有链地址)->地址b(以太坊公链地址)交易一方to地址为以太坊公链 3:表示私有链系统地址->转到私有链用户地址(用户获得的金币奖励)
     */
    private Integer type;
    /**
     * 备忘录
     */
    private String memo;

    /**
     * 重试次数
     */
    private Integer retry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }
}
