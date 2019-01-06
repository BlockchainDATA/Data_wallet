package com.blockchain.wallet.enums;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 下午1:55
 */
public enum TransactionTypeEnum {
    /**
     * 转入
     */
    TRANSACTION_IN(1),
    /**
     * 转出
     */
    TRANSACTION_OUT(2),
    /**
     * 主账户转出交易
     */
    MIN_ACCOUNT_OUT(3),

    /**
     * 私有链转到以太坊公链
     */
    PRIVATE_TO_PUBLIC(4);

    private final Integer type;

    TransactionTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
