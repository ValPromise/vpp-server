package com.vpp.core.standardized.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.OrderVo;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;

public interface IOrderService {

    int updateByPrimaryKeySelective(OrderList record);

    OrderList getOrderByOrderId(String innerOrderId);

    Page<OrderVo> getOrderList(Integer currentPage, Integer pageSize, Map<String, Object> params);

    OrderVo getOrderInfo(Map<String, Object> map);

    int insertOrder(OrderList order, OrderCity city);

    Page<OrderList> findLimitByTemplateIdEtime(int pageNum, int pageSize, String templateId, String etime);

    List<OrderCity> getOrderCityByInnerOrderId(String innerOrderId);

    int updatePayoutStateByInnerOrderId(String innerOrderId, BigDecimal payoutFee);

    Page<OrderList> getPaySuccessOrderByCustomerId(Integer currentPage, Integer pageSize, Long customerId);

    Page<OrderInfoVo> selectOrderList(Integer currentPage, Integer pageSize, Map<String, Object> params);

    OrderInfoVo selectOrderInfo(Map<String, Object> params);

    Page<OrderInfoVo> getSuccessOrdersByCondition(Integer currentPage, Integer pageSize, Long customerId, String productId);

    OrderInfoVo findOrderInfoVoByInnerOrderId(String innerOrderId) throws Exception;

    /**
     * 高温订单支付
     * 
     * @author Lxl
     * @param orderList
     * @param orderCity
     * @param customer
     */
    void tempContractPay(OrderList orderList, OrderCity orderCity, Customer customer) throws Exception;
}
