package com.blockchain.wallet.mapper;

import com.blockchain.wallet.entity.WithdrawPublicEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/11/22 5:17 PM
 */
@Repository
public interface IWithdrawPublicMapper {

    /**
     * 批量新增
     * @param withdrawPublicEntities
     * @return
     */
    Integer batchInsertWithdrawPublic(List<WithdrawPublicEntity> withdrawPublicEntities);

    /**
     * 新增
     * @param withdrawPublicEntity
     * @return
     */
    Integer insertWithdrawPublic(WithdrawPublicEntity withdrawPublicEntity);

    /**
     * 根据状态获取提现到公有链的交易
     * @param state
     * @return
     */
    List<WithdrawPublicEntity> findState(Integer state);

    /**
     * 批量更新
     * @param withdrawPublicEntities
     */
    void batchUpdateWithdrawPublic(List<WithdrawPublicEntity> withdrawPublicEntities);
    /**
     * 获取提现的交易详情
     * @param transactionId
     * @return
     */
    WithdrawPublicEntity findWithdrawDetail(String transactionId);
}
