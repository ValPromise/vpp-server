package com.vpp.core.deposit;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
@Mapper
public interface DepositMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Deposit record);

    int insertSelective(Deposit record);

    Deposit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Deposit record);

    int updateByPrimaryKey(Deposit record);
    
    Page<Deposit> selectDepositInfo(Map<String, Object> map);
    
    Long getMaxBlockNumber(String account);
}