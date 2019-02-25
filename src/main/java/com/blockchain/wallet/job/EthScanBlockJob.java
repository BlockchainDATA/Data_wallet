package com.blockchain.wallet.job;

import com.blockchain.wallet.entity.ScanBlockConfigEntity;
import com.blockchain.wallet.enums.ScanKeyEnum;
import com.blockchain.wallet.service.IScanBlockConfigService;
import com.blockchain.wallet.service.IScanBlockJobService;
import com.blockchain.wallet.utils.RandomUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

/**
 * 扫块定时任务
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/14 上午10:36
 */
@Component
@ConditionalOnExpression(" '${job.scanBlock.enable}' == 'true' ")
@Slf4j
public class EthScanBlockJob {
    @Value("${job.scanBlock.init-block-height}")
    private BigInteger initBlockHeight;
    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private IScanBlockConfigService scanBlockConfigService;

    @Value("#{'${privateEth.server}'.split(',')}")
    public  List<String> ethNodeList;
    @Resource
    private IScanBlockJobService scanBlockJobService;

    @Scheduled(fixedRateString = "${job.scanBlock.job-time}")
    public void scanBlockJob() {
        log.info("Scan Block Task Starts Execution...");
        //上一次扫描的区块
        ScanBlockConfigEntity scanBlockConfigEntity = scanBlockConfigService.getScanBlockConfig(ScanKeyEnum.SCAN_BLOCK_HEIGHT.getKey());
        //初始扫描区块
        BigInteger nextScanBlockHeight = initBlockHeight;
        if (null != scanBlockConfigEntity) {
            BigInteger blockHeight = scanBlockConfigEntity.getConfigValue();
            nextScanBlockHeight = blockHeight.add(BigInteger.valueOf(1));
        }
        //链上最高块
        BigInteger blockHeight = web3jUtil.getBlockHeight();
        if (null == blockHeight) {
            log.error("Acquisition Block High Failure...");
            return;
        }
        //缓存表中记录的最高块
        ScanBlockConfigEntity lastBlockEntity = scanBlockConfigService.getScanBlockConfig(ScanKeyEnum.LAST_BLOCK_HEIGHT.getKey());
        if (null == lastBlockEntity) {
            lastBlockEntity = new ScanBlockConfigEntity();
            lastBlockEntity.setConfigKey(ScanKeyEnum.LAST_BLOCK_HEIGHT.getKey());
            lastBlockEntity.setConfigValue(blockHeight);
            scanBlockConfigService.insert(lastBlockEntity);
        } else {
            if (0 > blockHeight.compareTo(nextScanBlockHeight)) {
                log.warn("Scanned to the highest block, block height:{}", nextScanBlockHeight);
                return;
            }
            if (0 < blockHeight.compareTo(lastBlockEntity.getConfigValue())) {
                lastBlockEntity.setConfigValue(blockHeight);
                scanBlockConfigService.update(lastBlockEntity);
            }
        }
        scanBlockJobService.scanBlock(nextScanBlockHeight, scanBlockConfigEntity);
    }
}
