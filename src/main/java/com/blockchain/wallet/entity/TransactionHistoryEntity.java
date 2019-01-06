package com.blockchain.wallet.entity;

import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 交易历史实体
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/12 上午11:00
 */
public class TransactionHistoryEntity {

    private Long id;
    /**
     * 交易ID
     */
    private String transactionId;
    /**
     * 交易hash
     */
    private String transactionHash;
    /**
     * from地址
     */
    private String transactionFrom;
    /**
     * to地址
     */
    private String transactionTo;
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
     * 交易类型 1：转入 2：转出
     */
    private Integer transactionType;
    /**
     * 交易签名
     */
    private String sign;
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
     * 块hash
     */
    private String blockHash;
    /**
     * 块高
     */
    private BigInteger blockNumber;
    /**
     * 块时间
     */
    private ZonedDateTime blockTime;


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

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTransactionFrom() {
        return transactionFrom;
    }

    public void setTransactionFrom(String transactionFrom) {
        this.transactionFrom = transactionFrom;
    }

    public String getTransactionTo() {
        return transactionTo;
    }

    public void setTransactionTo(String transactionTo) {
        this.transactionTo = transactionTo;
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

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public ZonedDateTime getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(ZonedDateTime blockTime) {
        this.blockTime = blockTime;
    }
}
