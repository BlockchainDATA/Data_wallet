package com.blockchain.wallet.enums;

/**
 * 提现交易类型
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 3:19 PM
 */
public enum TransactionOrderTypeEnum {
    /**
     * 私有链到私有链
     */
    PRIVATE_TO_PRIVATE(1),
    /**
     * 私有链到公有链
     */
    PRIVATE_TO_PUBLIC(2),
    /**
     * 私有链系统转到私有链用户(用户获取的奖励)
     */
    PRIVATE_SYSTEM_TO_PRIVATE_USER(3),

    /**
     * 系统内部构建的私有链->私有链交易
     */
    SYSTEM_BUILD_PRIVATE_TO_PRIVATE(4);

    private final Integer code;

    TransactionOrderTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
