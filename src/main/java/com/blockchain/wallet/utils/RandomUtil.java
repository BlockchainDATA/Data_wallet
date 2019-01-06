package com.blockchain.wallet.utils;

import java.util.Random;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/20 2:39 PM
 */
public class RandomUtil {

    /**
     * 获取一个指定范围随机整数
     *
     * @param size 范围
     * @return
     */
    public static Integer getRandomInt(Integer size) {
        Random rand = new Random();
        return rand.nextInt(size);
    }
}
