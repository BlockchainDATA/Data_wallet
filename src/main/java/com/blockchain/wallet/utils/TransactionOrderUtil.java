package com.blockchain.wallet.utils;

import com.blockchain.wallet.entity.TransactionOrderEntity;
import com.blockchain.wallet.entity.TransactionParamEntity;
import com.blockchain.wallet.enums.TransactionOrderStateEnum;
import com.blockchain.wallet.enums.TransactionOrderTypeEnum;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2019/1/14 3:03 PM
 */
public class TransactionOrderUtil {

    /**
     * 获取交易订单
     *
     * @param txParam 交易订单参数
     * @param txType  提现交易类型
     * @return
     */
    public static TransactionOrderEntity getTransactionOrder(TransactionParamEntity txParam, TransactionOrderTypeEnum txType, String gasLimit, String gasPrice) {

        TransactionOrderEntity txOrder = new TransactionOrderEntity();
        String transactionId = txParam.getTransactionId();
        if (StringUtils.isEmpty(transactionId)) {
            //生成交易订单ID
            transactionId = UUID.randomUUID().toString().replace("-", "");
        }
        txOrder.setTransactionId(transactionId);
        txOrder.setFromAddr(txParam.getFrom());
        txOrder.setToAddr(txParam.getTo());
        txOrder.setValue(txParam.getValue());
        txOrder.setMemo(txParam.getMemo());
        if (StringUtils.isEmpty(txParam.getFee())) {
            //给定一个默认手续费
            String fee = CurrencyMathUtil.multiply(gasLimit, gasPrice);
            txOrder.setFee(fee);
        } else {
            txOrder.setFee(txParam.getFee());
        }
        txOrder.setType(txType.getCode());
        txOrder.setState(TransactionOrderStateEnum.INIT.getCode());

        return txOrder;
    }
}
