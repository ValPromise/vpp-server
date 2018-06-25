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

    /**
     * 查询etime以前的未触发已支付的正常订单
     * 
     * @author Lxl
     * @param pageNum
     * @param pageSize
     * @param templateId
     * @param etime
     * @return
     */
    Page<OrderList> findLimitByTemplateIdEtime(int pageNum, int pageSize, String templateId, String etime);

    List<OrderCity> getOrderCityByInnerOrderId(String innerOrderId);

    int updatePayoutStateByInnerOrderId(String innerOrderId, BigDecimal payoutFee);

    Page<OrderList> getPaySuccessOrderByCustomerId(Integer currentPage, Integer pageSize, Long customerId);

    Page<OrderInfoVo> selectOrderList(Integer currentPage, Integer pageSize, Map<String, Object> params);

    OrderInfoVo selectOrderInfo(Map<String, Object> params);

    /**
     * app查询客户购买成功的正常订单
     * 
     * @author Lxl
     * @param currentPage
     * @param pageSize
     * @param customerId
     * @param productId
     * @return
     */
    Page<OrderInfoVo> getSuccessOrdersByCondition(Integer currentPage, Integer pageSize, Long customerId, String productId);

    /**
     * 根据条件分页查询order vo对象
     * 
     * @author Lxl
     * @param currentPage
     * @param pageSize
     * @param params
     * @return
     */
    Page<OrderInfoVo> findVosByCondition(Integer currentPage, Integer pageSize, Map<String, Object> params);

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

    /**
     * 降雨订单支付
     * 
     * @author Lxl
     * @param orderList
     * @param orderCity
     * @param customer
     */
    void rainContractPay(OrderList orderList, OrderCity orderCity, Customer customer) throws Exception;

    /**
     * 风控查询 - 单用户单城市单日购买次数
     * 
     * @author Lxl
     * @param customerId
     * @param cityId
     * @param date
     * @return
     */
    Integer fingRiskCountByCustomerIdCityId(String productId, Long customerId, String cityId, String date) throws Exception;

    /**
     * 风控查询 - 单用户单日购买次数
     * 
     * @author Lxl
     * @param customerId
     * @param cityId
     * @param date
     * @return
     */
    Integer fingRiskCountByCustomerId(String productId, Long customerId, String date) throws Exception;

    /**
     * 风控查询 - 单产品单日购买次数
     * 
     * @author Lxl
     * @param productId
     * @param date
     * @return
     */
    Integer fingRiskCountByProductId(String productId, String cityId, String date) throws Exception;

    Integer fingRiskCountByParams(Map<String, Object> params) throws Exception;
}
