package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.entity.TransactionHistoryEntity;
import com.blockchain.wallet.enums.AddressStateEnum;
import com.blockchain.wallet.enums.AddressTypeEnum;
import com.blockchain.wallet.enums.TransactionStateEnum;
import com.blockchain.wallet.enums.TransactionTypeEnum;
import com.blockchain.wallet.service.IAddressService;
import com.blockchain.wallet.service.IColdTransferJobService;
import com.blockchain.wallet.service.ITransactionHistoryService;
import com.blockchain.wallet.utils.CurrencyMathUtil;
import com.blockchain.wallet.utils.RandomUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/19 下午1:39
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ColdTransferJobServiceImpl implements IColdTransferJobService {
    @Resource
    private IAddressService addressService;
    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private ITransactionHistoryService transactionHistoryService;
    @Value("${job.mainAccount.min-balance}")
    private String minBalance;
    @Value("${job.mainAccount.max-balance}")
    private String maxBalance;
    @Value("${privateEth.gas-limit}")
    private String gasLimit;
    @Value("${privateEth.gas-price}")
    private String gasPrice;
    @Value("#{'${privateEth.server}'.split(',')}")
    public  List<String> ethNodeList;

    @Override
    public void coldTransfer() {
        //主账户的地址,只有一个
        List<AddressEntity> mainAddrList = addressService.findType(AddressTypeEnum.MAIN_ADDR.getCode());
        if (CollectionUtils.isEmpty(mainAddrList)) {
            log.info("No corresponding main account address was found");
            return;
        }
        AddressEntity mainAddr = mainAddrList.get(0);
        List<AddressEntity> systemAddrList = addressService.findType(AddressTypeEnum.SYSTEM_ADDR.getCode());
        List<TransactionHistoryEntity> txHistoryList = new ArrayList<>();
        systemAddrList = systemAddrList.stream().filter(systemAddr ->
                CurrencyMathUtil.compare(systemAddr.getBalance(), minBalance) == -1)
                .collect(Collectors.toList());
        for (AddressEntity systemAddr : systemAddrList) {
            if (AddressStateEnum.UNLOCK.getCode().equals(mainAddr.getState())) {
                String value = CurrencyMathUtil.subtract(maxBalance, systemAddr.getBalance());
                String transactionSign = web3jUtil.getETHTransactionSign(mainAddr.getPrivateKey(), mainAddr.getNonce(), systemAddr.getWalletAddress(), gasPrice, new BigInteger(gasLimit), value);
                String txHash = web3jUtil.getTxHash(systemAddr.getWalletAddress(),transactionSign, ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
                if (StringUtils.isEmpty(txHash)) {
                    log.error("Failed to get txHash,address:{}", mainAddr.getWalletAddress());
                    continue;
                }
                log.info("Transaction signature:{},Transaction Hash:{}", transactionSign, txHash);
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TransactionReceipt transactionReceipt = web3jUtil.getTransactionReceipt(txHash, ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
                    log.info("Transaction receipt for main account transfer", transactionReceipt);
                    if (!StringUtils.isEmpty(transactionReceipt)) {
                        //交易已经打包在块上
                        TransactionHistoryEntity txHistory = getTxHistory(mainAddr.getWalletAddress(), systemAddr.getWalletAddress(), txHash, transactionSign, value, mainAddr.getNonce());
                        txHistoryList.add(txHistory);
                        break;
                    }
                }
                mainAddr.setNonce(mainAddr.getNonce().add(BigInteger.valueOf(1L)));
            }
        }
        transactionHistoryService.batchInsertTxHistoryEntity(txHistoryList);
        //获取主账户余额
        String balance = web3jUtil.getBalance(mainAddr.getWalletAddress(), ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
        if (!StringUtils.isEmpty(balance)) {
            mainAddr.setBalance(balance);
        }
        BigInteger nonce = web3jUtil.getNonce(mainAddr.getWalletAddress(), ethNodeList.get(RandomUtil.getRandomInt(ethNodeList.size())));
        if (null != nonce) {
            mainAddr.setNonce(nonce);
        }
        mainAddr.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
        //更新主账户余额
        addressService.updateAddressEntity(mainAddr);
    }


    /**
     * 构建交易订单历史
     *
     * @param fromAddr from 地址
     * @param toAddr   to 地址
     * @param txHash   交易Hash
     * @param sign     交易签名
     * @param value    交易金额
     * @param nonce    nonce值
     * @return
     */
    private TransactionHistoryEntity getTxHistory(String fromAddr, String toAddr, String txHash, String sign, String value, BigInteger nonce) {
        TransactionHistoryEntity txHistory = new TransactionHistoryEntity();
        txHistory.setTransactionFrom(fromAddr);
        txHistory.setTransactionTo(toAddr);
        txHistory.setState(TransactionStateEnum.TRANSACTION_SUCCESS.getCode());
        txHistory.setTransactionHash(txHash);
        txHistory.setSign(sign);
        txHistory.setTransactionType(TransactionTypeEnum.MIN_ACCOUNT_OUT.getType());
        txHistory.setGasLimit(new BigInteger(this.gasLimit));
        txHistory.setGasPrice(this.gasPrice);
        txHistory.setValue(value);
        txHistory.setCreateTime(ZonedDateTime.now(ZoneOffset.UTC));
        txHistory.setNonce(nonce);
        return txHistory;
    }
}
