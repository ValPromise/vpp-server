/*
 * 文  件  名：BeanUtils.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月26日
 * 修改内容：新增
 */
package com.vpp.common.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;

/**
 * TODO 添加类的描述
 * 
 * @author Lxl
 * @version V1.0 2018年5月26日
 */
public class TempOrderBeanUtils {

    /**
     * 判断类型
     */
    public static final Byte OP_TYPE = 5;

    /**
     * 天气类型
     */
    public static final Byte WEATHER_TYPE = 1;

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
    public static OrderList getOrderList(Map<String, String> searchObj, Integer buyCnt, Long customerId, String contractId) {
        String price = searchObj.get("price").toString();
        String payoff = searchObj.get("payoff").toString();
        String threshold = searchObj.get("strike").toString();
        String time = searchObj.get("time").toString();
        Date stime = DateUtil.parseDate(time);
        Date etime = DateUtil.parseDate(DateUtil.addDate(time, 1));
        OrderList order = new OrderList();
        order.setBuyCount(buyCnt);
        order.setCustomerId(customerId);
        order.setGmtCreate(new Date());
        order.setInnerOrderId(contractId);
        order.setMaxPayout(priceMultiplyBuyCnt(parseString(payoff), buyCnt));
        order.setOpType(OP_TYPE);
        order.setWeatherType(WEATHER_TYPE);
        order.setTemplateId(searchObj.get("template_id").toString());
        order.setOrderPrice(priceMultiplyBuyCnt(parseString(price), buyCnt));
        order.setTriggerRuleParam(threshold);
        order.setOrderState(DEFAULT_ORDER_STATE);
        order.setStime(stime);
        order.setEtime(etime);
        order.setTriggerCheckState(DEFAULT_TRIGGER_CHECK_STATE);
        order.setProductId(ConstantsOrder.Product.PRODUCT_ID_TEMP);
        order.setPayState(ConstantsOrder.PAY_STATE_YES);
        order.setPayTime(new Date());
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
    public static OrderCity getOrderCity(Map<String, String> searchObj, String contractId) {
        String price = searchObj.get("price").toString();
        String payoff = searchObj.get("payoff").toString();
        String threshold = searchObj.get("strike").toString();
        String cityId = searchObj.get("cityId").toString();
        String time = searchObj.get("time").toString();
        Date stime = DateUtil.parseDate(time);
        Date etime = DateUtil.parseDate(DateUtil.addDate(time, 1));
        OrderCity city = new OrderCity();
        city.setInnerOrderId(contractId);
        city.setCityId(cityId);
        city.setThreshold(Float.valueOf(threshold));
        city.setStime(stime);
        city.setEtime(etime);
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
