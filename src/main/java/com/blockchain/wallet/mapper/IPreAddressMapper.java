package com.blockchain.wallet.mapper;

import com.blockchain.wallet.entity.PreAddressEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/30 3:37 PM
 */
@Repository
public interface IPreAddressMapper {
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
     *
     * @param address
     */
    void deletePreAddress(String address);

    /**
     * 获取表数据的数量
     * @return
     */
    Integer getCount();
}
