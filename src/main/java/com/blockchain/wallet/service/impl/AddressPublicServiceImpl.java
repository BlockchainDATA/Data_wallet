package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressPublicEntity;
import com.blockchain.wallet.mapper.IAddressPublicMapper;
import com.blockchain.wallet.service.IAddressPublicService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 6:18 PM
 */
@Service
public class AddressPublicServiceImpl implements IAddressPublicService {
    @Resource
    private IAddressPublicMapper addressPublicMapper;

    @Override
    public void updateAddressPublic(AddressPublicEntity addressPublicEntity) {
        addressPublicMapper.updateAddressPublic(addressPublicEntity);
    }

    @Override
    public void batchUpdateAddressPublic(List<AddressPublicEntity> addressPublicEntityList) {
        if (!CollectionUtils.isEmpty(addressPublicEntityList)) {
            addressPublicMapper.batchUpdateAddressPublic(addressPublicEntityList);
        }

    }

    @Override
    public List<AddressPublicEntity> findStateAndType(Integer state, Integer type) {
        return addressPublicMapper.findStateAndType(state, type);
    }

    @Override
    public List<AddressPublicEntity> findAddress(List<String> addressList) {
        if (!CollectionUtils.isEmpty(addressList)) {
            return addressPublicMapper.findAddress(addressList);
        }
        return null;
    }

    @Override
    public List<AddressPublicEntity> findStateAndTypeAndValue(Integer state, Integer type, String ethValue, String dtaValue) {
        return addressPublicMapper.findStateAndTypeAndValue(state, type, ethValue, dtaValue);
    }
}
