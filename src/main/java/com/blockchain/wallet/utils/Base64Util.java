package com.blockchain.wallet.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 加密
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/20 下午1:29
 */
public class Base64Util {
    /***
     * Base64加密
     * @param str 需要加密的参数
     * @return
     * @throws Exception
     */
    public static String encryptBase64(String str) {
        String result = null;
        try {
            result = Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * Base64解密
     * @param str 需要解密的参数
     * @return
     * @throws Exception
     */
    public static String decryptBase64(String str) {
        byte[] asBytes = Base64.getDecoder().decode(str);
        String result = null;
        try {
            result = new String(asBytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.decryptBase64("WVRUeDZYVkJjdA=="));

    }
}


