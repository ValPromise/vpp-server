/*
 * 文  件  名：BeanUtils.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月26日
 * 修改内容：新增
 */
package com.vpp.core.app.rain.utils;

import java.math.BigDecimal;

import com.vpp.common.utils.ConstantsOrder;
import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.DateUtil;
import com.vpp.core.standardized.order.bean.DataModelDto;
import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;

/**
 * 降雨合约bean工具类
 * 
 * @author Lxl
 * @version V1.0 2018年5月28日
 */
public class RainOrderBeanUtils {

    /**
     * 判断类型
     */
    public static final Byte OP_TYPE = 5;

    /**
     * 天气类型
     */
    public static final Byte WEATHER_TYPE = 4;

    /**
     * 默认订单状态 1：正常
     */
    public static final Byte DEFAULT_ORDER_STATE = 1;

    /**
     * 默认订单触发判断状态 0：为判断
     */
    public static final Byte DEFAULT_TRIGGER_CHECK_STATE = 0;

    /**
     * 根据合约封装高温合约订单bean
     * 
     * @author Lxl
     * @param searchObj
     * @param buyCnt
     * @param customerId
     * @param contractId
     * @return
     */
    public static OrderList getOrderList(DataModelDto dto, Integer buyCnt, Long customerId, String contractId) {
        String price = dto.getPrice().toString();
        String payoff = dto.getPayoutFee().toString();
        String threshold = dto.getThreshold().toString();
        OrderList order = new OrderList();
        order.setBuyCount(buyCnt);
        order.setCustomerId(customerId);
        order.setGmtCreate(DateUtil.getCurrentDateTimeLocal());
        order.setInnerOrderId(contractId);
        order.setMaxPayout(priceMultiplyBuyCnt(parseString(payoff), buyCnt));
        order.setOpType(OP_TYPE);
        order.setWeatherType(WEATHER_TYPE);
        order.setTemplateId(ConstantsRain.TEMPLATE_ID);
        order.setOrderPrice(priceMultiplyBuyCnt(parseString(price), buyCnt));
        order.setTriggerRuleParam(threshold);
        order.setOrderState(DEFAULT_ORDER_STATE);
        order.setStime(dto.getStime());
        order.setEtime(dto.getEtime());
        order.setTriggerCheckState(DEFAULT_TRIGGER_CHECK_STATE);
        order.setProductId(ConstantsRain.PRODUCT_ID);
        order.setPayState(ConstantsOrder.PAY_STATE_YES);
        order.setPayTime(DateUtil.getCurrentDateTimeLocal());
        order.setPayFee(order.getOrderPrice());

        return order;
    }

    /**
     * 高温合约封装订单城市bean
     * 
     * @author Lxl
     * @param searchObj
     * @param contractId
     * @return
     */
    public static OrderCity getOrderCity(DataModelDto dto, String contractId) {

        String price = dto.getPrice().toString();
        String payoff = dto.getPayoutFee().toString();

        OrderCity city = new OrderCity();
        city.setInnerOrderId(contractId);
        city.setCityId(dto.getCityId());
        city.setThreshold(dto.getThreshold());
        city.setStime(dto.getStime());
        city.setEtime(dto.getEtime());
        city.setOpType(OP_TYPE);
        city.setWeatherType(WEATHER_TYPE);
        city.setContractId(contractId);
        city.setContractPrice(parseString(price));
        city.setPayoutRuleParam(payoff);
        return city;
    }

    /**
     * String 转换为 BigDecimal
     * 
     * @author Lxl
     * @param price
     * @return
     */
    private static BigDecimal parseString(String price) {
        BigDecimal pr = new BigDecimal(price);
        return pr;
    }

    /**
     * 乘法
     * 
     * @author Lxl
     * @param price
     * @param buyCnt
     * @return
     */
    private static BigDecimal priceMultiplyBuyCnt(BigDecimal price, Integer buyCnt) {
        BigDecimal cnt = new BigDecimal(buyCnt);
        return cnt.multiply(price);
    }
}
