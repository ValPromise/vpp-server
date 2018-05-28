package com.vpp.core.deposit.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.vpp.core.deposit.bean.DepositAccount;
@Mapper
public interface DepositAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DepositAccount record);

    int insertSelective(DepositAccount record);

    DepositAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DepositAccount record);

    int updateByPrimaryKey(DepositAccount record);
}