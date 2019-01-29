package com.blockchain.wallet.utils;

import com.blockchain.wallet.entity.AddressEntity;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/29 1:49 PM
 */
public class SystemAddressUtil {
    public static ConcurrentLinkedQueue<AddressEntity> addressQueue = new ConcurrentLinkedQueue<>();
}
