package com.blockchain.wallet.job;

import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.enums.TransactionOrderStateEnum;
import com.blockchain.wallet.service.IBuildTransactionJobService;
import com.blockchain.wallet.service.ITransactionOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 构建交易的定时任务
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/17 下午3:40
 */
@Component
@ConditionalOnExpression(" '${job.transaction.enable}' == 'true' ")
@Slf4j
public class BuildTransactionJob {

    @Resource
    private IBuildTransactionJobService buildTransactionJobService;
    @Resource
    private ITransactionOrderService transactionOrderService;

    @Scheduled(fixedRateString = "${job.transaction.job-time}")
    public void buildTransactionJob() {
        log.info("Timing Task Opening for Constructing Transactions...");
        List<TransactionOrderEntity> txOrderList = transactionOrderService.findState(TransactionOrderStateEnum.INIT.getCode());
        if (CollectionUtils.isEmpty(txOrderList)) {
            log.info("No orders need to be built...");
            return;
        }
        buildTransactionJobService.buildTransaction(txOrderList);
    }
}
