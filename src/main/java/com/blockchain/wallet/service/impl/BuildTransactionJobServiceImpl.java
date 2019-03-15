package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.client.MoneyClient;
import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.handler.BuildTransactionHandler;
import com.blockchain.wallet.service.IAddressService;
import com.blockchain.wallet.service.IBuildTransactionJobService;
import com.blockchain.wallet.service.ITransactionHistoryService;
import com.blockchain.wallet.service.ITransactionOrderService;
import com.blockchain.wallet.utils.ExecutorServiceUtil;
import com.blockchain.wallet.utils.SystemAddressUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/17 下午3:52
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BuildTransactionJobServiceImpl implements IBuildTransactionJobService {
    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private IAddressService addressService;
    @Resource
    private ITransactionOrderService transactionOrderService;
    @Resource
    private ITransactionHistoryService transactionHistoryService;
    @Resource
    private MoneyClient moneyClient;
    @Value("${privateEth.gas-limit}")
    private String gasLimit;
    @Value("${publicEth.gas-limit}")
    private String publicGasLimit;
    @Value("${publicEth.gas-price}")
    private String publicGasPrice;
//    @Value("#{'${privateEth.server}'.split(',')}")
//    public List<String> ethNodeList;
//    @Resource
//    private RedissonClient redissonClient;

    @Override
    public void buildTransaction(List<TransactionOrderEntity> txOrderList) {
        ExecutorService executorService = ExecutorServiceUtil.executorService;
//        if (CollectionUtils.isEmpty(SystemAddressUtil.addressQueue)) {
//            addressService.setAddressQueue(AddressTypeEnum.SYSTEM_ADDR.getCode());
//        }
        BuildTransactionHandler buildTransactionHandler;
        for (TransactionOrderEntity txOrder : txOrderList) {
            ConcurrentLinkedQueue<AddressEntity> addressQueue = SystemAddressUtil.addressQueue;
            AddressEntity addressEntity = addressQueue.poll();
            if (Objects.isNull(addressEntity)) {
                continue;
            }
            log.info("This address is used to build transactions is:{}", addressEntity.getWalletAddress());
            buildTransactionHandler = new BuildTransactionHandler(addressService, web3jUtil, transactionOrderService, transactionHistoryService, txOrder, gasLimit, addressEntity, moneyClient);
            executorService.execute(buildTransactionHandler);
        }
        //关闭线程池
        //executorService.shutdown();
    }
}


