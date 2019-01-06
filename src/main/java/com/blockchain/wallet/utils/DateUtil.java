package com.blockchain.wallet.utils;

import java.util.Date;

/**
 * linux的时间转换
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/20 上午11:36
 */
public class DateUtil {
    /**
     * 转换linux的时间戳
     * @param timestamp
     * @return
     */
    public static Date getFormatDate(Long timestamp) {
        long time = timestamp * 1000;
        return new Date(time);
    }
}
