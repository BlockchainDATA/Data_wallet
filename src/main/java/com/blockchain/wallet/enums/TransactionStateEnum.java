package com.blockchain.wallet.enums;


/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 下午1:39
 */
public enum TransactionStateEnum {
    /**
     * 交易未处理
     */
    TRANSACTION_UNTREATED(1, "未处理"),
    /**
     * 交易签名
     */
    TRANSACTION_SIGNED(2, "交易已签名"),
    /**
     * 交易广播
     */
    TRANSACTION_BROADCAST(3, "交易已广播"),
    /**
     * 交易成功
     */
    TRANSACTION_SUCCESS(4, "交易成功"),
    /**
     * 交易失败
     */
    TRANSACTION_FAIL(5, "交易失败");

    private final Integer code;
    private final String name;

    TransactionStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
