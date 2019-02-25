package com.blockchain.wallet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.client.MoneyClient;
import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.entity.TransactionParamEntity;
import com.blockchain.wallet.enums.AwardStateEnum;
import com.blockchain.wallet.enums.TransactionOrderTypeEnum;
import com.blockchain.wallet.service.IBuildTransactionOrderJobService;
import com.blockchain.wallet.service.ITransactionOrderService;
import com.blockchain.wallet.utils.TransactionOrderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/14 2:48 PM
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BuildTransactionOrderJobServiceImpl implements IBuildTransactionOrderJobService {
    @Value("${privateEth.gas-limit}")
    private String gasLimit;
    @Value("${privateEth.gas-price}")
    private String gasPrice;
    @Resource
    private ITransactionOrderService transactionOrderService;
    @Resource
    private MoneyClient moneyClient;

    @Override
    public void buildTransactionOrder(List<TransactionParamEntity> dtaTransactionList) {
        List<TransactionOrderEntity> transactionOrderList = dtaTransactionList.stream()
                .map(txParam -> TransactionOrderUtil.getTransactionOrder(txParam, TransactionOrderTypeEnum.PRIVATE_SYSTEM_TO_PRIVATE_USER, this.gasLimit, this.gasPrice))
                .collect(Collectors.toList());
        transactionOrderService.batchInsertTxOrder(transactionOrderList);
        //更新奖励的状态
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionIds", transactionOrderList.stream().map(TransactionOrderEntity::getTransactionId).collect(Collectors.toList()));
        jsonObject.put("state", AwardStateEnum.WALLET_BUILD.getCode());
        moneyClient.updateAwardHistoryState(jsonObject);
    }


}
