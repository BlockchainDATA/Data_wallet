package com.blockchain.wallet.mapper;

import com.blockchain.wallet.entity.AddressPublicEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 6:18 PM
 */
@Repository
public interface IAddressPublicMapper {
    /**
     * 更新公有链地址信息
     *
     * @param addressPublicEntity
     */
    void updateAddressPublic(AddressPublicEntity addressPublicEntity);

    /**
     * 批量更新
     * @param addressPublicEntityList
     */
    void batchUpdateAddressPublic(List<AddressPublicEntity> addressPublicEntityList);

    /**
     * 根据状态和类型查询地址
     *
     * @param state
     * @param type
     * @return
     */
    List<AddressPublicEntity> findStateAndType(@Param("state") Integer state, @Param("type") Integer type);

    /**
     * 根据地址集合获取地址类型
     *
     * @param addressList
     * @return
     */
    List<AddressPublicEntity> findAddress(List<String> addressList);

    /**
     * 根据状态和类型查询地址和金额查询用户地址
     *
     * @param state    状态
     * @param type     类型
     * @param ethValue 消耗的eth的手续费
     * @param dtaValue 提现的dta的金额
     * @return
     */
    List<AddressPublicEntity> findStateAndTypeAndValue(@Param("state") Integer state, @Param("type") Integer type, @Param("ethValue") String ethValue, @Param("dtaValue") String dtaValue);
}
