package com.blockchain.wallet.utils;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.WalletApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApplication.class)
public class OkHttpUtilTest {

    @Resource
    private OkHttpUtil okHttpUtil;
    @Resource
    private Web3jUtil web3jUtil;

    @Test
    public void psotRequest() {
        JSONObject json = new JSONObject();
        json.put("addr","");
        json.put("password","");

        System.out.println(okHttpUtil.postRequest(json,""));
    }
    @Test
    public void getBalance(){
        System.out.println(web3jUtil.getBalance("",UrlConstUtil.ETH_NODE_LIST.get(RandomUtil.getRandomInt(UrlConstUtil.ETH_NODE_LIST.size()))));
    }

    @Test
    public void getTokenBalance(){
        System.out.println(web3jUtil.getTokenBalance(""));
    }
}