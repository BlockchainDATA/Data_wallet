package com.blockchain.wallet.client;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.entity.TransactionParamEntity;
import com.blockchain.wallet.utils.ActionResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/12/13 4:50 PM
 */
@FeignClient(value = "antube-money", path = "/award")
public interface MoneyClient {
//    @PostMapping("/updateDta/{walletAddress}")
//    Boolean updateDta(@PathVariable("walletAddress") String walletAddress, @RequestParam("dtaBalance") String dtaBalance);

    /**
     * 获取奖励Dta的交易
     *
     * @return
     */
    @GetMapping("/getDtaTransaction")
    ActionResult<List<TransactionParamEntity>> getDtaTransaction();

    /**
     * 更新奖励历史的奖励状态
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/updateAwardHistoryState")
    ActionResult<Integer> updateAwardHistoryState(@RequestBody JSONObject jsonObject);
}
