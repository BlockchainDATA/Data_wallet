package com.blockchain.wallet.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/26 6:22 PM
 */
public class UrlConstUtil {
    /**
     * 私有链eth节点的服务器
//     */
//    //@Value("${privateEth.server}")
//    public static  List<String> ETH_NODE_LIST;

//    static {
//        ETH_NODE_LIST = new ArrayList<>();
//        ETH_NODE_LIST.add("http://47.75.60.231:");
//        ETH_NODE_LIST.add("http://47.75.50.204:");
//        ETH_NODE_LIST.add("http://47.75.188.184:");
//    }
    /**
     * 获取私钥的端口+地址
     */
    public static final String PRIVATE_KEY_PORT = "8001/getPrivateKey";
    /**
     * 私有链节点地址
     */
    // public static final String ETH_PRIVATE_NODE_URL = "http://47.75.60.231:8545";
    /**
     * node服务获取地址私钥
     */
    // public static final String NODE_SERVICE_GET_PRIVATE_KEY = "http://47.75.60.231:8001/getPrivateKey";
    /**
     * node服务获取token余额
     */
    public static final String NODE_SERVICE_GET_TOKEN_BALANCE = "http://localhost:8002/getBalance";
    /**
     * 公有链节点地址(使用的nodeJS服务中的地址)
     */
    public static final String ETH_PUBLIC_NODE_URL = "https://ropsten.infura.io/VlzsGEDfjeD3lw1JthgE";

}
