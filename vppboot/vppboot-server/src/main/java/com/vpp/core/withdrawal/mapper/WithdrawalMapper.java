package com.vpp.core.withdrawal.mapper;

import java.util.Map;

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
}