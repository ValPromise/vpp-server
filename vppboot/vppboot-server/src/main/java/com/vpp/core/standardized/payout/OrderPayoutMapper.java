package com.vpp.core.standardized.payout;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

@Mapper
public interface OrderPayoutMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderPayout record);

    int insertSelective(OrderPayout record);

    OrderPayout selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderPayout record);

    int updateByPrimaryKey(OrderPayout record);

    Page<OrderPayout> selectOrderPayoutBypayoutState(@Param("payoutState") Byte payoutState);

    Page<OrderPayout> getPayOutHistoryCustomerId(@Param("customerId") Long customerId);

    Page<OrderPayout> findLimit(Map<String, Object> params);

}