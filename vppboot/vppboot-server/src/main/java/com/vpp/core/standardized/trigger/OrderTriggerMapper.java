package com.vpp.core.standardized.trigger;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface OrderTriggerMapper {
    int deleteByPrimaryKey(Long triggerId);

    int insert(OrderTrigger record);

    int insertSelective(OrderTrigger record);

    OrderTrigger selectByPrimaryKey(Long triggerId);

    int updateByPrimaryKeySelective(OrderTrigger record);

    int updateByPrimaryKey(OrderTrigger record);
    
    int insertOrderTriggerList(List<OrderTrigger> orderTriggers);
    
    OrderTrigger getTriggerByInnerOrderId(String innerOrderId);
}