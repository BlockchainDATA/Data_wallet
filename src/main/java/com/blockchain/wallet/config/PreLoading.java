package com.blockchain.wallet.config;

import com.blockchain.wallet.utils.HttpServiceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 预加载
 *
 * @author QiShuo
 * @version 1.0
 * @create 2019/2/22 3:13 PM
 */
@Component
public class PreLoading {
    @Value("#{'${privateEth.server}'.split(',')}")
    public List<String> ethNodeList;

    @PostConstruct
    public void setHttpService() {
        for (String url : ethNodeList) {
            HttpServiceUtil.httpServiceMap.put(url, new HttpService(url));
        }
    }
}
