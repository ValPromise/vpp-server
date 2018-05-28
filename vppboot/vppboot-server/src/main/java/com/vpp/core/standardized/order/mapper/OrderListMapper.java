package com.vpp.core.standardized.order.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.core.standardized.order.bean.OrderList;

@Mapper
public interface OrderListMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OrderList record);

    int insertSelective(OrderList record);

    OrderList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderList record);

    int updateByPrimaryKey(OrderList record);

    Page<OrderList> getOrderList(Map<String, Object> map);

    OrderList getOrderByOrderId(@Param("innerOrderId") String innerOrderId);

    OrderList getOrderInfoByOrderId(Map<String, Object> map);

    Page<OrderList> findLimitByTemplateIdEtime(@Param("templateId") String templateId, @Param("etime") String etime);

    int updateOrderTriggerByInnerOrderId(@Param("innerOrderId") String innerOrderId, @Param("payoutFee") BigDecimal payoutFee,
            @Param("triggerCheckState") Byte triggerCheckState);

    int updatePayoutStateByInnerOrderId(@Param("innerOrderId") String innerOrderId,
            @Param("realPayoutFee") BigDecimal realPayoutFee);

    Page<OrderList> getPaySuccessOrderByCustomerId(@Param("customerId") Long customerId);

    Page<OrderInfoVo> selectOrderList(Map<String, Object> params);

    OrderInfoVo selectOrderInfo(Map<String, Object> params);

    OrderInfoVo findOrderInfoVoByInnerOrderId(@Param("innerOrderId") String innerOrderId);

    Page<OrderInfoVo> getSuccessOrdersByCondition(@Param("customerId") Long customerId, @Param("productId") String productId);
}