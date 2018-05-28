package com.vpp.core.standardized.order;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface OrderCityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCity record);

    int insertSelective(OrderCity record);

    OrderCity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCity record);

    int updateByPrimaryKey(OrderCity record);
    
    List<OrderCity> getCitysByOrderId(String orderId);
}