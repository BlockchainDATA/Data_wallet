package com.blockchain.wallet.entity;

import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 地址实体
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/12 上午10:39
 */
public class AddressEntity {

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
     * nonce
     */
    private BigInteger nonce;
    /**
     * 余额
     */
    private String balance;
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
    /**
     * 地址类型 0:用户 1:系统 2:主账户
     */
    private Integer addrType;

    /**
     * 创建地址的密码
     */
    private String password;

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

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAddrType() {
        return addrType;
    }

    public void setAddrType(Integer addrType) {
        this.addrType = addrType;
    }

    public AddressEntity(String walletAddress, String privateKey, String password, Integer addrType) {
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
        this.password = password;
        this.addrType = addrType;
    }

    public AddressEntity() {

    }
}
