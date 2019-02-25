package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.service.IWalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WalletServiceImplTest {
    @Resource
    private IWalletService walletService;
//
//    @Test
//    public void createAddr() {
//        for (int i = 0; i < 100; i++) {
//            System.out.println(walletService.createAddr());
//        }
//    }
}