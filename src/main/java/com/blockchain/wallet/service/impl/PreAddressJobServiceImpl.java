package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.entity.PreAddressEntity;
import com.blockchain.wallet.service.IPreAddressJobService;
import com.blockchain.wallet.service.IPreAddressService;
import com.blockchain.wallet.utils.RandomUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/30 4:13 PM
 */
@Service
public class PreAddressJobServiceImpl implements IPreAddressJobService {

    @Value("${job.preAddress.number}")
    private Integer number;

    @Value("#{'${privateEth.server}'.split(',')}")
    public List<String> ethNodeList;

    @Resource
    private IPreAddressService preAddressService;

    @Resource
    private Web3jUtil web3jUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildPreAddress() {
        for (int i = 0; i < number; i++) {
            AddressEntity addr = web3jUtil.createAddr();
            PreAddressEntity preAddressEntity = new PreAddressEntity(addr.getWalletAddress(), addr.getPassword(), addr.getPrivateKey());
            preAddressService.insertPreAddress(preAddressEntity);
        }
    }
}
