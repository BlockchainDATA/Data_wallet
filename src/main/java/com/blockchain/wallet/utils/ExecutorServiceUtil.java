package com.blockchain.wallet.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/2/12 3:41 PM
 */
public class ExecutorServiceUtil {


    public static ExecutorService executorService = Executors.newFixedThreadPool(10);
}
