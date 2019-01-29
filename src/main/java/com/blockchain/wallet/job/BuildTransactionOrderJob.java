package com.blockchain.wallet.job;

import com.blockchain.wallet.client.MoneyClient;
import com.blockchain.wallet.entity.TransactionParamEntity;
import com.blockchain.wallet.service.IBuildTransactionOrderJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 构建交易订单job
 *
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/14 2:34 PM
 */
@Component
@ConditionalOnExpression(" '${job.txOrder.enable}' == 'true' ")
@Slf4j
public class BuildTransactionOrderJob {
    @Resource
    private MoneyClient moneyClient;
    @Resource
    private IBuildTransactionOrderJobService buildTransactionOrderJobService;
    @Scheduled(fixedRateString = "${job.txOrder.job-time}")
    public void buildTransactionOrder() {
        log.info("获取交易订单任务开始...");
        List<TransactionParamEntity> dtaTransactionList = moneyClient.getDtaTransaction().getData();
        if (CollectionUtils.isEmpty(dtaTransactionList)) {
            log.info("没有交易订单...");
            return;
        }
        buildTransactionOrderJobService.buildTransactionOrder(dtaTransactionList);
    }
}
