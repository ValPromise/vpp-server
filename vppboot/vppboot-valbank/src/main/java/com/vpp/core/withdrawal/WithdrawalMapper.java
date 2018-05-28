package com.vpp.core.withdrawal;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WithdrawalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Withdrawal record);

    int insertSelective(Withdrawal record);

    Withdrawal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Withdrawal record);

    int updateByPrimaryKey(Withdrawal record);
}