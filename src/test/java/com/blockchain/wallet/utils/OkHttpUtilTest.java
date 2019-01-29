package com.blockchain.wallet.utils;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.WalletApplication;
import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.mapper.IAddressMapper;
import com.blockchain.wallet.service.ITransactionOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApplication.class)
public class OkHttpUtilTest {

    @Resource
    private OkHttpUtil okHttpUtil;
    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private IAddressMapper addressMapper;
    @Resource
    private ITransactionOrderService transactionOrderService;

    @Value("#{'${privateEth.server}'.split(',')}")
    private List<String> ethNodeList;

//    @Resource
//    private RedissonClient redissonClient;

    @Test
    public void psotRequest() {
        JSONObject json = new JSONObject();
        json.put("addr", "0x9407fdd6218b8b7a9b8741f0e88b3f872877ae0f");
        json.put("password", "");

        System.out.println(okHttpUtil.postRequest(json, ""));
    }

    @Test
    public void getTokenBalance() {
        System.out.println(web3jUtil.getTokenBalance("0x46835BE24E640EEaa523ef6283990a255331782e"));
    }

    @Test
    public void updateAddress() {
        addressMapper.findType(1).forEach(addr -> {
            String balance = web3jUtil.getBalance(addr.getWalletAddress(), ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
            BigInteger nonce = web3jUtil.getNonce(addr.getWalletAddress(), ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
            addr.setState(0);
            addr.setBalance(balance);
            addr.setNonce(nonce);
            addressMapper.updateAddressEntity(addr);
        });
        addressMapper.findType(0).forEach(addr -> {
            String balance = web3jUtil.getBalance(addr.getWalletAddress(), ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
            BigInteger nonce = web3jUtil.getNonce(addr.getWalletAddress(), ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
            addr.setBalance(balance);
            addr.setNonce(nonce);
            addressMapper.updateAddressEntity(addr);
        });

    }

    @Test
    public void updateTxOrder() {
        ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue<>();
        transactionOrderService.findState(0).stream().map(TransactionOrderEntity::getTransactionId).forEach(queue::offer);
        queue.forEach(System.out::println);
        System.out.println(queue.size());
        String poll = queue.poll();
        System.out.println(poll);
        System.out.println(queue.size());
//        transactionOrderService.findState(0).forEach(txOrder -> {
//            txOrder.setRetry(0);
//            txOrder.setState(0);
//            transactionOrderService.updateTransactionOrder(txOrder);
//        });
    }
//
//    @Test
//    public void testRedisson() throws InterruptedException {
//        RLock rlock = redissonClient.getLock("test:");
//        System.out.println(rlock.isLocked());
//        rlock.lock(10, TimeUnit.SECONDS);
//        System.out.println(rlock.isLocked());
//        Thread.sleep(10000);
//        System.out.println("线程睡10s");
//        System.out.println(rlock.isLocked());
//    }
}