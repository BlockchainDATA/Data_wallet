package com.blockchain.wallet.service.impl;

import com.blockchain.wallet.entity.AddressPublicEntity;
import com.blockchain.wallet.entity.WithdrawPublicEntity;
import com.blockchain.wallet.enums.AddressStateEnum;
import com.blockchain.wallet.enums.TransactionStateEnum;
import com.blockchain.wallet.service.IAddressPublicService;
import com.blockchain.wallet.service.IPublicChainConfirmJobService;
import com.blockchain.wallet.service.IWithdrawPublicService;
import com.blockchain.wallet.utils.CurrencyMathUtil;
import com.blockchain.wallet.utils.UrlConstUtil;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * @create 2018/12/4 11:15 AM
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PublicChainConfirmJobServiceImpl implements IPublicChainConfirmJobService {
    @Resource
    private IWithdrawPublicService withdrawPublicService;
    @Resource
    private IAddressPublicService addressPublicService;
    @Resource
    private Web3jUtil web3jUtil;

    @Value("${publicEth.gas-limit}")
    private String publicGasLimit;
    @Value("${publicEth.gas-price}")
    private String publicGasPrice;

    @Override
    public void publicChainConfirm() {
        List<WithdrawPublicEntity> withdrawPublicList = withdrawPublicService.findState(TransactionStateEnum.TRANSACTION_BROADCAST.getCode());

        List<AddressPublicEntity> publicFromAddressList = addressPublicService.findAddress(withdrawPublicList.stream().map(WithdrawPublicEntity::getPublicFrom).collect(Collectors.toList()));
        List<AddressPublicEntity> updateAddressPublic = new ArrayList<>();
        List<WithdrawPublicEntity> updateWithdrawPublicList = new ArrayList<>();
        for (WithdrawPublicEntity withdrawPublic : withdrawPublicList) {
            String txHash = withdrawPublic.getPublicTransactionHash();
            TransactionReceipt transactionReceipt = web3jUtil.getTransactionReceipt(txHash, UrlConstUtil.ETH_PUBLIC_NODE_URL);
            if (null == transactionReceipt) {
                log.info("Transaction not confirmed,txHash:{}", txHash);
                continue;
            }
            AddressPublicEntity publicFrom = publicFromAddressList.stream().filter(publicFromAddress -> withdrawPublic.getPublicFrom().equals(publicFromAddress.getWalletAddress())).findFirst().orElse(null);
            if (publicFrom == null) {
                log.info("From address of public chain does not exist,fromPublicAddress:{}", withdrawPublic.getPublicFrom());
                continue;
            }
            withdrawPublic.setState(TransactionStateEnum.TRANSACTION_SUCCESS.getCode());
            BigInteger gasUsed = transactionReceipt.getGasUsed();
            withdrawPublic.setGasUsed(String.valueOf(gasUsed));
            withdrawPublic.setBlockNumber(transactionReceipt.getBlockNumber());
            withdrawPublic.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
            BigInteger gasLimit = withdrawPublic.getGasLimit();
            //未消耗的gas
            BigInteger unusedGas = gasLimit.subtract(gasUsed);
            if (unusedGas.compareTo(BigInteger.valueOf(0L)) > 0) {
                String paybackFee = CurrencyMathUtil.multiply(publicGasPrice, unusedGas.toString());
                String balance = CurrencyMathUtil.add(publicFrom.getEthBalance(), paybackFee);
                publicFrom.setEthBalance(balance);
            }
            //解锁地址
            publicFrom.setState(AddressStateEnum.UNLOCK.getCode());
            publicFrom.setUpdateTime(ZonedDateTime.now(ZoneOffset.UTC));
            updateAddressPublic.add(publicFrom);
            updateWithdrawPublicList.add(withdrawPublic);
        }
        addressPublicService.batchUpdateAddressPublic(updateAddressPublic);
        withdrawPublicService.batchUpdateWithdrawPublic(updateWithdrawPublicList);
    }
}
