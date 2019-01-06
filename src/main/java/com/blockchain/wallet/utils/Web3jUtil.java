package com.blockchain.wallet.utils;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.entity.AddressEntity;
import com.blockchain.wallet.enums.AddressTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * web3j的工具类
 *
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/12 下午1:30
 */
@Component
@Slf4j
public class Web3jUtil {
    @Resource
    private OkHttpUtil okHttpUtil;

    @Value("${erc.accuracy}")
    private Integer accuracy;


    /**
     * 创建钱包地址
     *
     * @return
     */
    public AddressEntity createAddr(String ethNodeUrl) {
        Admin admin = Admin.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            //获取随机密码
            String password = randomString();
            NewAccountIdentifier response = admin.personalNewAccount(password).send();
            if (checkResult(response)) {
                log.error("Failure to create user address, reason:{}", response.getError().getMessage());
                return null;
            }
            String accountId = response.getAccountId();
            String privateKey = getAddresPrivateKey(accountId, password, ethNodeUrl);
            if (StringUtils.isEmpty(privateKey)) {
                log.error("Failed to obtain the user's private key, address:{}", accountId);
                return null;
            }
            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setPrivateKey(privateKey);
            addressEntity.setWalletAddress(accountId);
            //进行加密
            password = Base64Util.encryptBase64(password);
            addressEntity.setPassword(password);
            addressEntity.setAddrType(AddressTypeEnum.USER_ADDR.getCode());
            return addressEntity;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            admin.shutdown();
        }
        return null;
    }

    /**
     * 根据私钥获得用户地址
     *
     * @param privateKey 用户的私钥
     * @return
     */
    public String getAccount(String privateKey) {
        BigInteger privateKeyInt = new BigInteger(privateKey, 16);
        ECKeyPair keyPair = ECKeyPair.create(privateKeyInt);
        return Keys.getAddress(keyPair);
    }

    /**
     * 获取账户余额单位 eth
     *
     * @param addr
     * @param ethNodeUrl 节点地址
     * @return
     */
    public String getBalance(String addr, String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            EthGetBalance response = web3j.ethGetBalance(addr, DefaultBlockParameterName.LATEST).send();
            if (checkResult(response)) {
                log.error("Failed to obtain address balance, reason:{}", response.getError().getMessage());
                return null;
            }
            BigInteger balanceWei = response.getBalance();
            return CurrencyMathUtil.weiToEth(String.valueOf(balanceWei));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    /**
     * 获取代币余额
     *
     * @param address
     * @return
     */
    public String getTokenBalance(String address) {
        JSONObject json = new JSONObject();
        json.put("addr", address);
        String tokenBalance = okHttpUtil.postRequest(json, UrlConstUtil.NODE_SERVICE_GET_TOKEN_BALANCE);
        return CurrencyMathUtil.divide(tokenBalance, String.valueOf(Math.pow(10, accuracy)));
    }


    /**
     * 获取交易签名(ETH)
     *
     * @param fromPrivateKey from 地址的私钥
     * @param fromNonce      from地址的nonce
     * @param toAddr         to地址
     * @param gasPrice       单位eth
     * @param gasLimit       gasgLimit
     * @param value          金额 单位eth
     * @return
     */
    public String getETHTransactionSign(String fromPrivateKey, BigInteger fromNonce, String toAddr, String gasPrice,
                                        BigInteger gasLimit, String value) {
        BigInteger valueWei = new BigInteger(CurrencyMathUtil.ethToWei(value));
        BigInteger gasPriceWei = new BigInteger(CurrencyMathUtil.ethToWei(gasPrice));
        // 附加信息，交易额外携带的数据，也将写入区块永存
        String data = "";
        RawTransaction rawTx = RawTransaction.createTransaction(fromNonce, gasPriceWei, gasLimit, toAddr, valueWei, data);
        Credentials credentials = Credentials.create(fromPrivateKey);
        //获取交易签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTx, credentials);
        return Numeric.toHexString(signedMessage);
    }

    /**
     * 获取交易签名(ERC20)
     *
     * @param fromPrivateKey  from地址的私钥
     * @param fromNonce       from地址的nonce
     * @param contractAddress 合约地址
     * @param toAddr          to地址
     * @param gasPrice        gasPrice
     * @param gasLimit        gasLimit
     * @param value           代币的金额
     * @return
     */
    public String getERCTransactionSign(String fromPrivateKey, BigInteger fromNonce, String contractAddress, String toAddr, String gasPrice,
                                        BigInteger gasLimit, BigInteger value) {
        BigInteger gasPriceWei = new BigInteger(CurrencyMathUtil.ethToWei(gasPrice));
        //加载交易所需的凭证使用fromKey
        Credentials credentials = Credentials.create(fromPrivateKey);
        //Function第一个参数为合约转账的方法名,第二个参数主要是input参数的List,集合中放置真正的to地址和交易的token金额
        //第三个参数为output参数
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddr), new Uint256(value)),
                Arrays.asList(new TypeReference<Type>() {
                }));
        String encode = FunctionEncoder.encode(function);
        //创建RawTransaction交易对象
        RawTransaction rawTx = RawTransaction.createTransaction(fromNonce, gasPriceWei, gasLimit, contractAddress, encode);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTx, credentials);
        return Numeric.toHexString(signedMessage);
    }

    /**
     * 广播交易获取txHash
     *
     * @param hexValue   交易签名
     * @param ethNodeUrl 节点地址
     * @return
     */
    public String getTxHash(String hexValue, String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        String txHash = null;
        try {
            EthSendTransaction response = web3j.ethSendRawTransaction(hexValue).send();
            if (checkResult(response)) {
                log.error("Broadcasting Deal Failed,reason:{}", response.getError().getMessage());
                return null;
            }
            txHash = response.getTransactionHash();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return txHash;
    }

    /**
     * 获取块高
     *
     * @param ethNodeUrl 节点地址
     * @return
     */
    public BigInteger getBlockHeight(String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            EthBlockNumber response = web3j.ethBlockNumber().send();
            if (checkResult(response)) {
                log.error("Acquisition Block High Failure,reason:{}", response.getError().getMessage());
            }
            return response.getBlockNumber();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    /**
     * 获取nonce值
     *
     * @param addr       地址
     * @param ethNodeUrl 节点地址
     * @return
     */
    public BigInteger getNonce(String addr, String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            EthGetTransactionCount response = web3j.ethGetTransactionCount(addr, DefaultBlockParameterName.LATEST).send();
            if (checkResult(response)) {
                log.error("Failed to get nonce value,reason:{}", response.getError().getMessage());
                return null;
            }
            return response.getTransactionCount();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    /**
     * 根据交易hash获取交易收据
     *
     * @param txHash     交易hash
     * @param ethNodeUrl 节点地址
     * @return
     */
    public TransactionReceipt getTransactionReceipt(String txHash, String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            EthGetTransactionReceipt response = web3j.ethGetTransactionReceipt(txHash).send();
            if (checkResult(response)) {
                log.error("Failure to obtain transaction receipt,reason:{}", response.getError().getMessage());
                return null;
            }
            return response.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    /**
     * 根据交易hash获取交易详情
     *
     * @param txHash     交易hash
     * @param ethNodeUrl 节点地址
     * @return
     */
    public Transaction getTransactionByHash(String txHash, String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            EthTransaction response = web3j.ethGetTransactionByHash(txHash).send();
            if (checkResult(response)) {
                log.error("Failure to obtain transaction details,reason:{}", response.getError().getMessage());
            }
            return response.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    /**
     * 扫块
     *
     * @param blockHeight 块高
     * @param ethNodeUrl  节点地址
     * @return
     */
    public EthBlock scanBlock(BigInteger blockHeight, String ethNodeUrl) {
        Web3j web3j = Web3j.build(new HttpService(ethNodeUrl + UrlConstUtil.GETH_PORT));
        try {
            EthBlock response = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockHeight), false).send();
            if (checkResult(response)) {
                log.error("Failed to obtain block information,reason:{}", response.getError().getMessage());
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    /**
     * 验证地址是否地址有效
     *
     * @param addr
     * @return
     */
    public Boolean checkAddr(String addr) {
        return WalletUtils.isValidAddress(addr);
    }

    /**
     * 检查返回结果
     *
     * @param response
     * @return
     */
    private Boolean checkResult(Response response) {
        if (null != response.getResult()) {
            return false;
        } else {
            return null != response.getError();
        }
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @return
     */
    private String randomString() {
        char[] code = new char[10];
        for (int i = 0; i < 10; i++) {
            code[i] = Consts.DIGITS_UPPER[(int) (Math.random() * 62)];
        }
        return String.valueOf(code);
    }


    /**
     * 获取地址的私钥
     *
     * @param addr
     * @param password
     * @return
     */
    private String getAddresPrivateKey(String addr, String password, String ethNodeUrl) {
        JSONObject json = new JSONObject();
        json.put("addr", addr);
        json.put("password", password);
        return okHttpUtil.postRequest(json, ethNodeUrl + UrlConstUtil.PRIVATE_KEY_PORT);
    }

}