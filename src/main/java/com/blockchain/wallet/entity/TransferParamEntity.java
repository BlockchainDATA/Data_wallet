package com.blockchain.wallet.entity;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/12/28 3:35 PM
 */
public class TransferParamEntity {
    private String fromAddress;
    private String toAddress;
    private String privateKey;
    private String value;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
