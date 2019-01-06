package com.blockchain.wallet.entity;

import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 6:11 PM
 */
public class AddressPublicEntity {

    private Long id;
    /**
     * 地址
     */
    private String walletAddress;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 地址类型 1:系统 2:主账户
     */
    private Integer addrType;
    /**
     * nonce
     */
    private BigInteger nonce;
    /**
     * ETH的余额
     */
    private String ethBalance;
    /**
     * DTA的余额
     */
    private String dtaBalance;
    /**
     * 状态，0：未锁定 1：锁定
     */
    private Integer state;
    /**
     * 创建时间
     */
    private ZonedDateTime createTime = ZonedDateTime.now(ZoneOffset.UTC);
    /**
     * 修改时间
     */
    private ZonedDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Integer getAddrType() {
        return addrType;
    }

    public void setAddrType(Integer addrType) {
        this.addrType = addrType;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getEthBalance() {
        return ethBalance;
    }

    public void setEthBalance(String ethBalance) {
        this.ethBalance = ethBalance;
    }

    public String getDtaBalance() {
        return dtaBalance;
    }

    public void setDtaBalance(String dtaBalance) {
        this.dtaBalance = dtaBalance;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
