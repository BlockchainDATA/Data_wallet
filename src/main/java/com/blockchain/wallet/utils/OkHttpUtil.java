package com.blockchain.wallet.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 上午10:27
 */
@Component
public class OkHttpUtil {
    @Resource
    private OkHttpClient okHttpClient;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public String postRequest(JSONObject params, String url) {
        RequestBody requestBody = RequestBody.create(JSON, params.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            return null != responseBody ? responseBody.string() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
