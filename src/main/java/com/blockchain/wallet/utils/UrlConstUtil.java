package com.blockchain.wallet.utils;

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
     */
    public static final List<String> ETH_NODE_LIST;

    static {
        ETH_NODE_LIST = new ArrayList<>();
        // http://id:
        ETH_NODE_LIST.add("");
        ETH_NODE_LIST.add("");
        ETH_NODE_LIST.add("");
    }

    /**
     * geth节点的端口
     */
    public static final String GETH_PORT = "";
    /**
     * 获取私钥的端口+地址 {端口号}/{接口}
     */
    public static final String PRIVATE_KEY_PORT = "";

    /**
     * node服务获取token余额
     */
    public static final String NODE_SERVICE_GET_TOKEN_BALANCE = "";
    /**
     * 公有链节点地址(使用的nodeJS服务中的地址)
     */
    public static final String ETH_PUBLIC_NODE_URL = "";

}
