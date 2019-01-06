package com.blockchain.wallet.service;

import com.blockchain.wallet.entity.AddressEntity;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 下午4:19
 */
public interface IAddressService {
    /**
     * 新增地址信息
     *
     * @param addressEntity
     */
    void insertAddressEntity(AddressEntity addressEntity);

    /**
     * 根据地址查询用户
     *
     * @param addr
     * @return
     */
    AddressEntity findAddress(String addr);

    /**
     * 根据用户地址与状态查找
     *
     * @param addr  地址
     * @param state 状态
     * @return
     */
    AddressEntity findAddrAndState(String addr, Integer state);

    /**
     * 更新用户地址
     *
     * @param addressEntity
     */
    void updateAddressEntity(AddressEntity addressEntity);

    /**
     * 根据地址更新余额
     * @param addr
     * @param balance
     */
    void updateBalance(String addr, String balance, ZonedDateTime dateTime);

    /**
     * 批量更新用户地址
     *
     * @param addrEntityList
     */
    void batchUpdateAddressList(List<AddressEntity> addrEntityList);

    /**
     * 根据用户地址列表查询用户
     *
     * @param addrList
     * @return
     */
    List<AddressEntity> findAddressList(List<String> addrList);


    /**
     * 根据状态和类型查询地址
     *
     * @param state
     * @param type
     * @return
     */
    List<AddressEntity> findStateAndType(Integer state, Integer type);

    /**
     * 根据类型查询地址
     *
     * @param type
     * @return
     */
    List<AddressEntity> findType(Integer type);

    /**
     * 根据状态和类型查询地址和金额查询用户地址
     * @param state 状态
     * @param type 类型
     * @param value 金额
     * @return
     */
    List<AddressEntity> findStateAndTypeAndValue(Integer state, Integer type,String value);

}
