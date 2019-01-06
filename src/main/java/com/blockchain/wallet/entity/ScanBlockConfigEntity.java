package com.blockchain.wallet.entity;

import java.math.BigInteger;

/**
 * 扫描块时的缓存表
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/12 上午11:14
 */
public class ScanBlockConfigEntity {
    /**
     * key
     */
    private String configKey;
    /**
     * value
     */
    private BigInteger configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public BigInteger getConfigValue() {
        return configValue;
    }

    public void setConfigValue(BigInteger configValue) {
        this.configValue = configValue;
    }
}
