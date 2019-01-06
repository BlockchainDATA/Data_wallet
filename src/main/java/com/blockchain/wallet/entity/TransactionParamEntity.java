package com.blockchain.wallet.entity;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/17 下午3:15
 */
public class TransactionParamEntity {
    /**
     * 交易id
     */
    private String transactionId;
    private String from;
    private String to;
    private String value;
    private String fee;
    /**
     * 备忘录
     */
    private String memo;
    /**
     * 提现订单类型1:表示用户a->用户b双方都是私有链操作,2:表示地址a(私有链地址)->地址b(以太坊公链地址)交易一方to地址为以太坊公链 ,3:表示私有链系统地址->转到私有链用户地址(用户获得的金币奖励)
     */
    private Integer type;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
}
