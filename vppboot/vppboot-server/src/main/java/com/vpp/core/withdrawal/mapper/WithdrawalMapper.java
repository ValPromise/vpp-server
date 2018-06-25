package com.vpp.core.withdrawal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.withdrawal.bean.Withdrawal;

public interface WithdrawalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Withdrawal record);

    int insertSelective(Withdrawal record);

    Withdrawal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Withdrawal record);

    int updateByPrimaryKey(Withdrawal record);

    Page<Withdrawal> selectWithdrawalList(Map<String, Object> map);

    /**
     * 根据支付账号，收款账号查询最大区块号
     * 
     * @author Lxl
     * @param payerAddress
     * @param payeeAddress
     * @return
     */
    Long findMaxBlockNumber();

    List<Withdrawal> findListByAddress(@Param("payerAddress") String payerAddress, @Param("payeeAddress") String payeeAddress);

    /**
     * 查询所有待处理的提现 状态 2：已打款
     * 
     * @author Lxl
     * @return
     */
    List<Withdrawal> findWaitListByAddress();
}