package com.blockchain.wallet.entity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/30 3:29 PM
 */
public class PreAddressEntity {
    private Long id;
    private String address;
    private String password;
    private String privateKey;
    private ZonedDateTime createTime = ZonedDateTime.now(ZoneOffset.UTC);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public PreAddressEntity(String address, String password, String privateKey) {
        this.address = address;
        this.password = password;
        this.privateKey = privateKey;
    }

    public PreAddressEntity() {
    }
}
