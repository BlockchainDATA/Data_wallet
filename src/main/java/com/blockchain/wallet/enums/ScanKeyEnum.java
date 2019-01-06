package com.blockchain.wallet.enums;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 上午10:49
 */
public enum ScanKeyEnum {

    /** 当前最新区块高度 */
    LAST_BLOCK_HEIGHT("eth_block_height"),
    /** 前一次扫描的区块高度 */
    SCAN_BLOCK_HEIGHT("eth_scan_block_height");

    private final String key;
    ScanKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
