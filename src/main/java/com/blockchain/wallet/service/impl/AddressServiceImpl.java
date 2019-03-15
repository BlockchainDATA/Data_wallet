package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.enums.AddressTypeEnum;
import com.blockchain.wallet.mapper.IAddressMapper;
import com.blockchain.wallet.service.IAddressService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.List;

import static com.blockchain.wallet.utils.SystemAddressUtil.addressQueue;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 下午4:20
 */
@Service
public class AddressServiceImpl implements IAddressService {

    @Resource
    private IAddressMapper addressEntityMapper;

    //public static ConcurrentLinkedQueue<AddressEntity> addressQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void insertAddressEntity(AddressEntity addressEntity) {
        addressEntityMapper.insertAddressEntity(addressEntity);
    }

    @Override
    public AddressEntity findAddress(String addr) {
        return addressEntityMapper.findAddress(addr);
    }

    @Override
    public AddressEntity findAddrAndState(String addr, Integer state) {
        return addressEntityMapper.findAddrAndState(addr, state);
    }

    @Override
    public void updateAddressEntity(AddressEntity addressEntity) {
        addressEntityMapper.updateAddressEntity(addressEntity);
    }

    @Override
    public void updateBalance(String addr, String balance, ZonedDateTime dateTime) {
        addressEntityMapper.updateBalance(addr, balance, dateTime);
    }

    @Override
    public void batchUpdateAddressList(List<AddressEntity> addrEntityList) {
        if (!CollectionUtils.isEmpty(addrEntityList)) {
            addressEntityMapper.batchUpdateAddressList(addrEntityList);
        }

    }

    @Override
    public List<AddressEntity> findAddressList(List<String> addrList) {
        if (CollectionUtils.isEmpty(addrList)) {
            return null;
        }
        return addressEntityMapper.findAddressList(addrList);
    }

    @Override
    public List<AddressEntity> findStateAndType(Integer state, Integer type) {
        return addressEntityMapper.findStateAndType(state, type);
    }

    @Override
    public List<AddressEntity> findType(Integer type) {
        return addressEntityMapper.findType(type);
    }

    @Override
    public List<AddressEntity> findStateAndTypeAndValue(Integer state, Integer type, String value) {
        return addressEntityMapper.findStateAndTypeAndValue(state, type, value);
    }

    @Override
    @PostConstruct
    public void setAddressQueue() {
        findType(AddressTypeEnum.SYSTEM_ADDR.getCode()).forEach(addressQueue::offer);
    }
}
