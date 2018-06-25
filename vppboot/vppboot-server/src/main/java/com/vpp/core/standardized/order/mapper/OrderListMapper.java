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

    /**
     * 根据id修改触发后的订单信息
     * 
     * @author Lxl
     * @param innerOrderId
     * @param payoutFee
     * @param realPayoutFee
     * @param triggerCheckState
     * @return
     */
    int updateTriggerSuccessByInnerOrderId(@Param("innerOrderId") String innerOrderId, @Param("payoutFee") BigDecimal payoutFee,
            @Param("realPayoutFee") BigDecimal realPayoutFee, @Param("triggerCheckState") Byte triggerCheckState);

    int updatePayoutStateByInnerOrderId(@Param("innerOrderId") String innerOrderId,
            @Param("realPayoutFee") BigDecimal realPayoutFee);

    Page<OrderList> getPaySuccessOrderByCustomerId(@Param("customerId") Long customerId);

    Page<OrderInfoVo> selectOrderList(Map<String, Object> params);

    OrderInfoVo selectOrderInfo(Map<String, Object> params);

    OrderInfoVo findOrderInfoVoByInnerOrderId(@Param("innerOrderId") String innerOrderId);

    Page<OrderInfoVo> getSuccessOrdersByCondition(@Param("customerId") Long customerId, @Param("productId") String productId);

    /**
     * 根据条件查询
     * 
     * @author Lxl
     * @param params
     * @return
     */
    Page<OrderInfoVo> findVosByCondition(Map<String, Object> params);

    /**
     * 风控查询 - 单用户单城市单日购买次数
     * 
     * @author Lxl
     * @param customerId
     * @param cityId
     * @param date
     * @return
     */
    Integer fingRiskCountByCustomerIdCityId(@Param("productId") String productId, @Param("customerId") Long customerId,
            @Param("cityId") String cityId, @Param("date") String date);

    /**
     * 风控查询 - 单用户单日购买次数
     * 
     * @author Lxl
     * @param productId
     * @param customerId
     * @param date
     * @return
     */
    Integer fingRiskCountByCustomerId(@Param("productId") String productId, @Param("customerId") Long customerId,
            @Param("date") String date);

    /**
     * 风控查询 - 单产品单日购买次数
     * 
     * @author Lxl
     * @param productId
     * @param date
     * @return
     */
    Integer fingRiskCountByProductId(@Param("productId") String productId, @Param("cityId") String cityId,
            @Param("date") String date);

    /**
     * 根据条件查询已购买的数量
     * 
     * @author Lxl
     * @param params
     * @return
     */
    Integer fingRiskCountByParams(Map<String, Object> params);
}