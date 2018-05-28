package com.vpp.core.standardized.order;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.vo.OrderInfoVo;

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

}