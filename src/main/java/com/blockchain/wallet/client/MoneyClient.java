//package com.blockchain.wallet.client;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
///**
// * @author QiShuo
// * @version 1.0
// * @create 2018/12/13 4:50 PM
// */
//@FeignClient(value = "antube-money", path = "/account")
//public interface MoneyClient {
//    @PostMapping("/updateDta/{walletAddress}")
//    Boolean updateDta(@PathVariable("walletAddress") String walletAddress, @RequestParam("dtaBalance") String dtaBalance);
//}
