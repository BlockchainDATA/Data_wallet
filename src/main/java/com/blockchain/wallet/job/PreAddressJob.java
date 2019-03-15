package com.blockchain.wallet.job;

import com.blockchain.wallet.service.IPreAddressJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 预生产地址
 *
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/30 4:00 PM
 */
@Component
@ConditionalOnExpression(" '${job.preAddress.enable}' == 'true' ")
@Slf4j
public class PreAddressJob {
    @Resource
    private IPreAddressJobService preAddressJobService;

    @Scheduled(cron = "0 15 19 * * ?")
    public void buildPreAddress() {
        log.info("Batch Generation Address Task Starts...");
        preAddressJobService.buildPreAddress();
        log.info("Batch Generation Address Task End...");
    }
}
