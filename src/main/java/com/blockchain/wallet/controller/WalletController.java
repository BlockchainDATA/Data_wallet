package com.blockchain.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.wallet.entity.*;
import com.blockchain.wallet.service.IWalletService;
import com.blockchain.wallet.utils.ActionResult;
import com.blockchain.wallet.utils.Web3jUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/13 上午11:22
 */
@RestController
@RequestMapping("/wallet")
@Slf4j
public class WalletController {
    @Resource
    private IWalletService walletService;
    @Resource
    private Web3jUtil web3jUtil;

    /**
     * 创建地址
     *
     * @return
     */
    @GetMapping("/createAddr")
    public ActionResult<String> createAddr() {
        String addr = walletService.createAddr(null);
        if (StringUtils.isEmpty(addr)) {
            return ActionResult.New(1, "创建用户失败");
        }
        log.info("Generated wallet address:{}", addr);
        return ActionResult.New(addr);
    }

    /**
     * 构建交易
     *
     * @param txParam 交易订单
     * @return
     */
    @PostMapping("/buildTransaction")
    public ActionResult<String> buildTransaction(@RequestBody TransactionParamEntity txParam) {
        if (StringUtils.isEmpty(txParam.getTo()) ||
                StringUtils.isEmpty(txParam.getValue())) {
            return ActionResult.New(1, "参数为空");
        }
        if (StringUtils.isEmpty(txParam.getFrom())) {
            if (web3jUtil.checkAddr(txParam.getTo())) {
                return ActionResult.New(walletService.buildTransaction(txParam));
            }
        } else {
            if (web3jUtil.checkAddr(txParam.getFrom()) && web3jUtil.checkAddr(txParam.getTo())) {
                return ActionResult.New(walletService.buildTransaction(txParam));
            }
        }
        return ActionResult.New(1, "地址错误");

    }

    /**
     * 获取地址转出交易
     *
     * @param addr 地址
     * @return
     */
    @GetMapping("/getAddressOutTransaction/{addr}")
    public ActionResult getAddressOutTransaction(@PathVariable("addr") String addr) {
        if (StringUtils.isEmpty(addr)) {
            return ActionResult.New(1, "参数为空");
        } else {
            List<TransactionHistoryEntity> addressOutTransactionList = walletService.findAddressOutTransaction(addr);
            if (CollectionUtils.isEmpty(addressOutTransactionList)) {
                return ActionResult.New(1, "没有数据");
            }
            return ActionResult.New(addressOutTransactionList);
        }
    }

    /**
     * 获取地址的转入交易
     *
     * @param addr 地址
     * @return
     */
    @GetMapping("/getAddressInTransaction/{addr}")
    public ActionResult getAddressInTransaction(@PathVariable("addr") String addr) {
        if (StringUtils.isEmpty(addr)) {
            return ActionResult.New(1, "参数为空");
        } else {
            List<TransactionHistoryEntity> addressInTransactionList = walletService.findAddressInTransaction(addr);
            if (CollectionUtils.isEmpty(addressInTransactionList)) {
                return ActionResult.New(1, "没有数据");
            }
            return ActionResult.New(addressInTransactionList);
        }
    }

    /**
     * 根据地址获取地址实体
     *
     * @param addr 地址
     * @return
     */
    @GetMapping("/getAddrInfo/{addr}")
    public ActionResult<AddressEntity> getAddrInfo(@PathVariable("addr") String addr) {
        if (StringUtils.isEmpty(addr)) {
            return ActionResult.New(1, "参数为空");
        } else {
            return ActionResult.New(walletService.queryAddressEntity(addr));
        }
    }

    /**
     * 根据状态和订单类型获取交易订单
     *
     * @param state 订单状态
     * @param type  订单类型
     * @return
     */
    @GetMapping("/getTransactionOrder")
    public ActionResult<List<TransactionOrderEntity>> getTransactionOrderList(@RequestParam("state") String state, @RequestParam("type") String type) {
        if (StringUtils.isEmpty(state) && StringUtils.isEmpty(type)) {
            return ActionResult.New(1, "参数为空");
        } else {
            return ActionResult.New(walletService.getTransactionOrderList(Integer.valueOf(state), Integer.valueOf(type)));
        }
    }

    /**
     * 后台管理处理提现交易后台的交易
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/handlerPublicWithdrawOrder")
    public ActionResult<String> handlerPublicWithdrawOrder(@RequestBody JSONObject jsonObject) {
        String transactionId = jsonObject.getString("transactionId");
        Integer state = jsonObject.getInteger("state");
        if (StringUtils.isEmpty(transactionId) && Objects.isNull(state)) {
            return ActionResult.New(1, "参数为空");
        } else {
            return ActionResult.New(walletService.handlerPublicWithdrawOrder(transactionId, state));
        }
    }

    /**
     * 获取提现订单
     *
     * @param fromAddress
     * @return
     */
    @GetMapping("/getAddressWithdrawList/{fromAddr}")
    public ActionResult<List<TransactionOrderEntity>> getAddressWithdrawOrderList(@PathVariable("fromAddr") String fromAddress) {
        if (StringUtils.isEmpty(fromAddress)) {
            return ActionResult.New(1, "参数为空");
        }
        return ActionResult.New(walletService.getAddressWithdrawOrderList(fromAddress));
    }

    /**
     * 获取公有链的提现订单详情
     *
     * @param transactionId
     * @return
     */
    @GetMapping("/getWithdrawDetail/{transactionId}")
    public ActionResult<WithdrawPublicEntity> getWithdrawDetail(@PathVariable("transactionId") String transactionId) {
        if (StringUtils.isEmpty(transactionId)) {
            return ActionResult.New(1, "参数为空");
        }
        return ActionResult.New(walletService.getWithdrawDetail(transactionId));
    }

    /**
     * 根据交易ID获取私有链上的交易历史
     *
     * @param transactionId
     * @return
     */
    @GetMapping("/getPrivateChainTransaction/{transactionId}")
    public ActionResult<TransactionHistoryEntity> getPrivateChainTransaction(@PathVariable("transactionId") String transactionId) {
        if (StringUtils.isEmpty(transactionId)) {
            return ActionResult.New(1, "参数为空");
        }
        return ActionResult.New(walletService.getPrivateChainTransaction(transactionId));
    }

    /**
     * 使用私钥进行直接转账
     *
     * @param paramEntity
     * @return
     */
    @PostMapping("/privateKeyTransfer")
    public ActionResult<String> privateKeyTransfer(@RequestBody TransferParamEntity paramEntity) {
        if (StringUtils.isEmpty(paramEntity.getFromAddress()) || StringUtils.isEmpty(paramEntity.getToAddress())
                || StringUtils.isEmpty(paramEntity.getPrivateKey()) || StringUtils.isEmpty(paramEntity.getValue())) {
            return ActionResult.New(1, "参数为空");
        }
        return ActionResult.New(walletService.privateKeyTransfer(paramEntity.getFromAddress(), paramEntity.getToAddress(), paramEntity.getPrivateKey(), paramEntity.getValue()));
    }

    @GetMapping("/scanBlockConfig")
    public ActionResult<Map<String, BigInteger>> getScanBlockConfig() {
        return ActionResult.New(walletService.getScanBlockConfig());
    }

    @GetMapping("/batchCreateAddr")
    public Boolean batchCreateAddr(Integer type, Integer number) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (int i = 0; i < number; i++) {
                walletService.createAddr(type);
            }
        });
        return true;
    }
}
