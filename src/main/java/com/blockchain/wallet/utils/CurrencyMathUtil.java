package com.blockchain.wallet.utils;

import org.web3j.utils.Convert;

import java.math.BigDecimal;

import static com.sun.webkit.graphics.GraphicsDecoder.SCALE;

/**
 * 数的转换
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 下午3:35
 */
public class CurrencyMathUtil {


    /**
     * wei转eth
     * @param wei
     * @return
     */
    public static String weiToEth(String wei) {
        BigDecimal eth = Convert.fromWei(wei,Convert.Unit.ETHER);
        return eth.stripTrailingZeros().toPlainString();
    }

    /**
     * eth转换wei
     * @param eth
     * @return
     */
    public static String ethToWei(String eth){
        BigDecimal wei = Convert.toWei(eth,Convert.Unit.ETHER);
        return wei.stripTrailingZeros().toPlainString();
    }
    /**
     * gwei转换为eth
     * @param gwei
     * @return
     */
    public static String gweiToEth(String gwei){
        BigDecimal wei=Convert.toWei(gwei,Convert.Unit.GWEI);
        return weiToEth(wei.stripTrailingZeros().toPlainString());
    }
    /**
     * 比较大小 str1大于str2返回1， 相等返回0, a小于b返回-1
     * @param str1
     * @param str2
     * @return
     */
    public static int compare(String str1, String str2) {
        BigDecimal a = new BigDecimal(str1).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(str2).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        return a.compareTo(b);
    }
    /**
     * 大数乘法
     * @param str1
     * @param str2
     * @return
     */
    public static String multiply(String str1, String str2) {
        BigDecimal a = new BigDecimal(str1).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(str2).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal c = a.multiply(b).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        return c.stripTrailingZeros().toPlainString();
    }

    /**
     * 大数加法
     * @param str1
     * @param str2
     * @return
     */
    public static String add(String str1, String str2) {
        BigDecimal a = new BigDecimal(str1).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(str2).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal c = a.add(b).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        return c.stripTrailingZeros().toPlainString();
    }

    /**
     * 大数除法
     * @param str1
     * @param str2
     * @return
     */
    public static String divide(String str1, String str2) {
        BigDecimal a = new BigDecimal(str1).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(str2).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal c = a.divide(b, SCALE, BigDecimal.ROUND_HALF_DOWN);
        return c.stripTrailingZeros().toPlainString();
    }

    /**
     * 大数减法
     * @param str1
     * @param str2
     * @return
     */
    public static String subtract(String str1, String str2) {
        BigDecimal a = new BigDecimal(str1).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(str2).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal c = a.subtract(b).setScale(SCALE, BigDecimal.ROUND_HALF_DOWN);
        return c.stripTrailingZeros().toPlainString();
    }
}
