package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.*;
import com.blockchain.wallet.enums.AddressTypeEnum;
import com.blockchain.wallet.enums.ScanKeyEnum;
import com.blockchain.wallet.enums.TransactionOrderTypeEnum;
import com.blockchain.wallet.enums.TransactionTypeEnum;
import com.blockchain.wallet.service.*;
import com.blockchain.wallet.utils.CurrencyMathUtil;
import com.blockchain.wallet.utils.RandomUtil;
import com.blockchain.wallet.utils.TransactionOrderUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 上午11:07
 */
@Service
@Slf4j
public class WalletServiceImpl implements IWalletService {
    @Resource
    private Web3jUtil web3jUtil;
    @Resource
    private IAddressService addressService;
    @Resource
    private ITransactionHistoryService transactionHistoryService;
    @Resource
    private ITransactionOrderService transactionOrderService;

    @Resource
    private IWithdrawPublicService withdrawPublicService;
    @Resource
    private IBlockInfoService blockInfoService;
    @Resource
    private IScanBlockConfigService scanBlockConfigService;

    @Value("${privateEth.gas-limit}")
    private String gasLimit;
    @Value("${privateEth.gas-price}")
    private String gasPrice;
    @Value("#{'${privateEth.server}'.split(',')}")
    public List<String> ethNodeList;
    @Resource
    private IPreAddressService preAddressService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAddr(Integer type) {
        Integer count = preAddressService.getCount();
        AddressEntity addressEntity;
        if (count == null || count == 0) {
            log.info("No pre-production address...");
            addressEntity = web3jUtil.createAddr();
        } else {
            PreAddressEntity preAddress = preAddressService.getPreAddress();
            addressEntity = new AddressEntity(preAddress.getAddress(), preAddress.getPrivateKey(), preAddress.getPassword(), AddressTypeEnum.USER_ADDR.getCode());
            preAddressService.deletePreAddress(preAddress.getAddress());
        }

        if (null == addressEntity) {
            log.error("Failed to create user address");
            return null;
        }
        if (!Objects.isNull(type)) {
            addressEntity.setAddrType(type);
        }
        addressService.insertAddressEntity(addressEntity);
        return addressEntity.getWalletAddress();
    }

    @Override
    public AddressEntity queryAddressEntity(String addr) {
        return addressService.findAddress(addr);
    }

    @Override
    public String buildTransaction(TransactionParamEntity txParam) {
        TransactionOrderEntity transactionOrder;
        AddressEntity fromAddressEntity;
        if (StringUtils.isEmpty(txParam.getFrom())) {
            //使用系统默认的手续费进行转账
            String fee = CurrencyMathUtil.multiply(this.gasLimit, this.gasPrice);
            txParam.setFee(fee);
            transactionOrder = TransactionOrderUtil.getTransactionOrder(txParam, TransactionOrderTypeEnum.PRIVATE_SYSTEM_TO_PRIVATE_USER, this.gasLimit, this.gasPrice);
            //添加交易订单
            transactionOrderService.insertTransactionOrder(transactionOrder);
            return transactionOrder.getTransactionId();
        }
        fromAddressEntity = addressService.findAddress(txParam.getFrom());
        if (Objects.isNull(fromAddressEntity)) {
            log.info("From address does not exist ,address:{}", txParam.getFrom());
            return null;
        }
        String balance = fromAddressEntity.getBalance();
        if (CurrencyMathUtil.compare(balance, CurrencyMathUtil.add(txParam.getValue(), txParam.getFee())) > -1) {
            if (TransactionOrderTypeEnum.PRIVATE_TO_PRIVATE.getCode().equals(txParam.getType())) {
                AddressEntity toAddressEntity = addressService.findAddress(txParam.getTo());
                if (Objects.isNull(toAddressEntity)) {
                    log.info("To address does not exist,address:{}", txParam.getTo());
                    return null;
                }
                transactionOrder = TransactionOrderUtil.getTransactionOrder(txParam, TransactionOrderTypeEnum.PRIVATE_TO_PRIVATE, this.gasLimit, this.gasPrice);
            } else {
                transactionOrder = TransactionOrderUtil.getTransactionOrder(txParam, TransactionOrderTypeEnum.PRIVATE_TO_PUBLIC, this.gasLimit, this.gasPrice);
            }
            //添加交易订单
            transactionOrderService.insertTransactionOrder(transactionOrder);
            return transactionOrder.getTransactionId();
        } else {
            log.info("The address balance is insufficient:{}", txParam.getFrom());
            return null;
        }
    }


    @Override
    public List<TransactionHistoryEntity> findAddressOutTransaction(String addr) {
        return transactionHistoryService.findAddressTransaction(addr, TransactionTypeEnum.TRANSACTION_OUT.getType());
    }

    @Override
    public List<TransactionHistoryEntity> findAddressInTransaction(String addr) {
        return transactionHistoryService.findAddressTransaction(addr, TransactionTypeEnum.TRANSACTION_IN.getType());
    }

    @Override
    public List<TransactionOrderEntity> getTransactionOrderList(Integer state, Integer type) {
        return transactionOrderService.findStateAndType(state, type);
    }

    @Override
    public String handlerPublicWithdrawOrder(String transactionId, Integer state) {
        transactionOrderService.updateTransactionOrderEntity(transactionId, state);
        return transactionId;
    }

    @Override
    public List<TransactionOrderEntity> getAddressWithdrawOrderList(String fromAddress) {
        return transactionOrderService.findFromAddress(fromAddress, TransactionOrderTypeEnum.PRIVATE_TO_PUBLIC.getCode());
    }

    @Override
    public WithdrawPublicEntity getWithdrawDetail(String transactionId) {
        return withdrawPublicService.findWithdrawDetail(transactionId);
    }

    @Override
    public TransactionHistoryEntity getPrivateChainTransaction(String transactionId) {

        return Optional.ofNullable(transactionHistoryService.findTransactionHistory(transactionId)).map(transactionHistory -> {
            if (transactionHistory.getBlockNumber() != null) {
                BlockInfoEntity blockInfo = blockInfoService.findByBlockNumber(transactionHistory.getBlockNumber());
                if (!Objects.isNull(blockInfo)) {
                    transactionHistory.setBlockTime(blockInfo.getBlockTime());
                }
            }
            return transactionHistory;
        }).orElse(null);
    }

    @Override
    public String privateKeyTransfer(String fromAddress, String toAddress, String privateKey, String value) {
        BigInteger nonce = web3jUtil.getNonce(fromAddress);
        //获取交易签名
        String txSign = web3jUtil.getETHTransactionSign(privateKey, nonce, toAddress, this.gasPrice, new BigInteger(this.gasLimit), value);
        if (StringUtils.isEmpty(txSign)) {
            log.error("Transaction signature failed,fromAddress:{}", fromAddress);
            return null;
        }
        String txHash = web3jUtil.getTxHash(fromAddress, txSign);
        if (StringUtils.isEmpty(txHash)) {
            log.error("Transaction broadcasting failed,fromAddress:{}", fromAddress);
            return null;
        }
        log.info("Transaction success awaits confirmation,fromAddress:{},txHash:{}", fromAddress, txHash);
        return txHash;
    }

    @Override
    public Map<String, BigInteger> getScanBlockConfig() {
        Map<String, BigInteger> result = new HashMap<>();
        //最新块高
        ScanBlockConfigEntity ethBlockHeight = scanBlockConfigService.getScanBlockConfig(ScanKeyEnum.LAST_BLOCK_HEIGHT.getKey());
        //当前扫描块高
        ScanBlockConfigEntity ethScanBlockHeight = scanBlockConfigService.getScanBlockConfig(ScanKeyEnum.SCAN_BLOCK_HEIGHT.getKey());

        result.put(ethBlockHeight.getConfigKey(), ethBlockHeight.getConfigValue());
        result.put(ethScanBlockHeight.getConfigKey(), ethScanBlockHeight.getConfigValue());
        return result;
    }

}
