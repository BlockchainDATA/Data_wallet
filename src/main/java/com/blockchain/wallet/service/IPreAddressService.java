package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.PreAddressEntity;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/30 3:31 PM
 */
public interface IPreAddressService {
    /**
     * 获取一个地址
     *
     * @return
     */
    PreAddressEntity getPreAddress();

    /**
     * 新增一个地址
     *
     * @param preAddress
     */
    void insertPreAddress(PreAddressEntity preAddress);

    /**
     * 批量新增地址
     *
     * @param preAddressList
     */
    void batchInsertPreAddress(List<PreAddressEntity> preAddressList);

    /**
     * 根据地址进行删除
     * @param address
     */
    void deletePreAddress(String address);

    /**
     * 获取表的数量
     * @return
     */
    Integer getCount();
}
