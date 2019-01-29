package com.blockchain.wallet.enums;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/14 3:14 PM
 */
public enum AwardStateEnum {
    /**
     * 奖励初始化
     */
    INIT(0),
    /**
     * 奖励成功
     */
    SUCCESS(1),
    /**
     * 钱包构建中
     */
    WALLET_BUILD(2),
    /**
     * 奖励失败
     */
    FAIL(3);
    private final Integer code;

    AwardStateEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
