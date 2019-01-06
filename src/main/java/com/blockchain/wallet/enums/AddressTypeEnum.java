package com.blockchain.wallet.enums;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/17 下午6:42
 */
public enum AddressTypeEnum {
    /**
     * 用户地址
     */
    USER_ADDR(0),
    /**
     * 系统地址
     */
    SYSTEM_ADDR(1),
    /**
     * 主账户地址
     */
    MAIN_ADDR(2);

    private final Integer code;

    AddressTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }


}
