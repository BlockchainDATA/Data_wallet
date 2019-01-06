package com.blockchain.wallet.entity;

import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 提现至以太坊公有链记录
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 5:10 PM
 */
public class WithdrawPublicEntity {

    private Long id;
    /**
     * 交易ID
     */
    private String transactionId;

    /**
     * 系统构建的私有链交易ID
     */
    private String privateTransactionId;
    /**
     * 公有链交易hash
     */
    private String publicTransactionHash;
    /**
     * 公有链from地址
     */
    private String publicFrom;
    /**
     * 公有链to地址
     */
    private String publicTo;
    /**
     * 私有链的from地址
     */
    private String privateFrom;
    /**
     * 私有链的to地址
     */
    private String privateTo;
    /**
     * 交易金额
     */
    private String value;
    /**
     * gas的单价
     */
    private String gasPrice;
    /**
     * gasLimit
     */
    private BigInteger gasLimit;
    /**
     * 交易真正消耗的gas
     */
    private String gasUsed;
    /**
     * nonce
     */
    private BigInteger nonce;
    /**
     * 交易状态 1：未处理，2：交易被签名，3：交易被广播，4：交易成功，5：交易失败
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
     * 块高
     */
    private BigInteger blockNumber;
    /**
     * 描述
     */
    private String memo;

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

    public String getPublicTransactionHash() {
        return publicTransactionHash;
    }

    public void setPublicTransactionHash(String publicTransactionHash) {
        this.publicTransactionHash = publicTransactionHash;
    }

    public String getPublicFrom() {
        return publicFrom;
    }

    public void setPublicFrom(String publicFrom) {
        this.publicFrom = publicFrom;
    }

    public String getPublicTo() {
        return publicTo;
    }

    public void setPublicTo(String publicTo) {
        this.publicTo = publicTo;
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public void setPrivateFrom(String privateFrom) {
        this.privateFrom = privateFrom;
    }

    public String getPrivateTo() {
        return privateTo;
    }

    public void setPrivateTo(String privateTo) {
        this.privateTo = privateTo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getPrivateTransactionId() {
        return privateTransactionId;
    }

    public void setPrivateTransactionId(String privateTransactionId) {
        this.privateTransactionId = privateTransactionId;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
