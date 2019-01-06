package com.blockchain.wallet.job;

import com.blockchain.wallet.service.IBuildPublicChainTransferJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/26 4:12 PM
 */
@Component
@ConditionalOnExpression(" '${job.publicChain.build.enable}' == 'true' ")
@Slf4j
public class BuildPublicChainTransferJob {
    @Resource
    private IBuildPublicChainTransferJobService publicChainTransferJobService;
    @Scheduled(fixedRateString = "${job.publicChain.build.job-time}")
    public void publicChainTransfer() {
        log.info("Establishing the Public Chain Transaction Task and Starting to Execute...");
        publicChainTransferJobService.buildPublicChainTransfer();
    }
}
