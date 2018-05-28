package com.vpp.core.standardized.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.vo.OrderInfoVo;
import com.vpp.vo.OrderVo;

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

}
