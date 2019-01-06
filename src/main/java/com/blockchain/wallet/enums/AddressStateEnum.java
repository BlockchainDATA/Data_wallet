package com.blockchain.wallet.enums;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 下午6:31
 */
public enum AddressStateEnum {
    /**
     * 未锁定
     */
    UNLOCK(0),
    /**
     * 锁定
     */
    LOCK(1);

    private final Integer code;
    AddressStateEnum (Integer code){
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
}
