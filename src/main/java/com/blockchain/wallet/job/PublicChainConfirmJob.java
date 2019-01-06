package com.blockchain.wallet.job;

import com.blockchain.wallet.service.IPublicChainConfirmJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 公有链的交易确认
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/12/4 11:08 AM
 */
@Component
@ConditionalOnExpression(" '${job.publicChain.confirm.enable}' == 'true' ")
@Slf4j
public class PublicChainConfirmJob {
    @Resource
    private IPublicChainConfirmJobService publicChainConfirmJobService;

    /**
     * 公有链交易确认
     */
    @Scheduled(fixedRateString = "${job.publicChain.confirm.job-time}")
    public void publicChainConfirm() {
        log.info("The transaction confirmation timer task of public chain starts to execute...");
        publicChainConfirmJobService.publicChainConfirm();
    }
}
