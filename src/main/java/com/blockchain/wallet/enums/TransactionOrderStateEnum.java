package com.blockchain.wallet.enums;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/18 下午2:19
 */
public enum TransactionOrderStateEnum {
    /**
     * 初始
     */
    INIT(0),
    /**
     * 处理中
     */
    HANDLING(1),
    /**
     * 成功
     */
    SUCCESS(2),
    /**
     * 失败
     */
    FAIL(3),

    /**
     * 拒绝
     */
    REFUSED(4),

    /**
     * 审核通过
     */
    PASS(5);

    private final Integer code;
    TransactionOrderStateEnum(Integer code){
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }


}
