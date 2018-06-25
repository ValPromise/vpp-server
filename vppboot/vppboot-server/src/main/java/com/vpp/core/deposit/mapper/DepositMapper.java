package com.vpp.core.deposit.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.vpp.core.deposit.bean.Deposit;

@Mapper
public interface DepositMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Deposit record);

    int insertSelective(Deposit record);

    Deposit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Deposit record);

    int updateByPrimaryKey(Deposit record);

    Page<Map<String, Object>> selectDepositInfo(Map<String, Object> map);

    Page<Deposit> findLimit(Map<String, Object> map);

    Long getMaxBlockNumber(String account);
}