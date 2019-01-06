package com.blockchain.wallet.job;

import com.blockchain.wallet.service.IColdTransferJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主账户转账任务
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/18 下午5:32
 */
@Component
@ConditionalOnExpression(" '${job.mainAccount.enable}' == 'true' ")
@Slf4j
public class ColdTransferJob {

    @Resource
    private IColdTransferJobService coldTransferJobService;

    @Scheduled(fixedRateString = "${job.mainAccount.job-time}")
    public void coldTransferJob() {
        log.info("Main Account Distribution Task Begins...");
        coldTransferJobService.coldTransfer();
    }
}
