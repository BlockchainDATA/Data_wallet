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
        json.put("addr","0x9407fdd6218b8b7a9b8741f0e88b3f872877ae0f");
        json.put("password","123");

        System.out.println(okHttpUtil.postRequest(json,"http://47.75.60.231:8001/getPrivateKey"));
    }
    @Test
    public void getBalance(){
        System.out.println(web3jUtil.getBalance("0x9407fdd6218b8b7a9b8741f0e88b3f872877ae0f",UrlConstUtil.ETH_NODE_LIST.get(RandomUtil.getRandomInt(UrlConstUtil.ETH_NODE_LIST.size()))));
    }

    @Test
    public void getTokenBalance(){
        System.out.println(web3jUtil.getTokenBalance("0x46835BE24E640EEaa523ef6283990a255331782e"));
    }
}