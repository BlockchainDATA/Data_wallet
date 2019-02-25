package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.PreAddressEntity;
import com.blockchain.wallet.mapper.IPreAddressMapper;
import com.blockchain.wallet.service.IPreAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/30 3:51 PM
 */
@Service
public class PreAddressServiceImpl implements IPreAddressService {
    @Resource
    private IPreAddressMapper preAddressMapper;

    @Override
    public PreAddressEntity getPreAddress() {
        return preAddressMapper.getPreAddress();
    }

    @Override
    public void insertPreAddress(PreAddressEntity preAddress) {
        preAddressMapper.insertPreAddress(preAddress);
    }

    @Override
    public void batchInsertPreAddress(List<PreAddressEntity> preAddressList) {
        preAddressMapper.batchInsertPreAddress(preAddressList);
    }

    @Override
    public void deletePreAddress(String address) {
        preAddressMapper.deletePreAddress(address);
    }

    @Override
    public Integer getCount() {
        return preAddressMapper.getCount();
    }
}
