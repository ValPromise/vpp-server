package com.vpp.core.cashlog.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.vpp.core.cashlog.bean.CustomerCashLog;

@Mapper
public interface CustomerCashLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerCashLog record);

    int insertSelective(CustomerCashLog record);

    CustomerCashLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerCashLog record);

    int updateByPrimaryKey(CustomerCashLog record);

    Page<CustomerCashLog> findLimitByCondition(Map<String, Object> params);
}