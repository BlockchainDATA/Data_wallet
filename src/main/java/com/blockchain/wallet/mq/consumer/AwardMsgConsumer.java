package com.blockchain.wallet.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.entity.TransactionParamEntity;
import com.blockchain.wallet.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 奖励的消费者
 * @author QiShuo
 * @version 1.0
 * @create 2018/12/5 6:36 PM
 */
//@Component
@Slf4j
public class AwardMsgConsumer {
    @Resource
    private IWalletService walletService;

    /**
     * 接收消息
     * @param text
     */
    //@JmsListener(destination = "award.msg.queue")
    public void receive(String text) {
        log.info("AwardMsgConsumer收到的报文为:" + text);
        TransactionParamEntity transactionParamEntity = JSONObject.parseObject(text, TransactionParamEntity.class);
        walletService.buildTransaction(transactionParamEntity);
    }
}
