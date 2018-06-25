/*
 * 文  件  名：AppUtils.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月26日
 * 修改内容：新增
 */
package com.vpp.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.vpp.common.vo.AppOrderInfoVo;
import com.vpp.common.vo.AppOrderListVo;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.core.standardized.trigger.OrderTrigger;

/**
 * APP工具类
 * 
 * @author Lxl
 * @version V1.0 2018年5月26日
 */
public class AppUtils {
    /**
     * 封装app合约列表对象
     * 
     * @author Lxl
     * @param orders
     * @return
     * @throws Exception
     */
    public static List<AppOrderListVo> getAppVoList(List<OrderInfoVo> orders) throws Exception {
        List<AppOrderListVo> vos = new ArrayList<AppOrderListVo>();
        for (OrderInfoVo vo : orders) {
            String cityCnName = vo.getCityCnName();
            String st = DateUtil.strToStr(vo.getStime(), DateUtil.LONG_DATE_TIME_PATTERN, "MMdd");

            String weatherName = WeatherUtils.getSymbolChinese(vo.getWeatherType());
            String symbol = OpTypeUtils.getSymbol(vo.getOpType());
            String threshold = vo.getThreshold().toString();
            String unit = WeatherUtils.getUnit(vo.getWeatherType());

            String title = cityCnName + st;
            String content = weatherName + symbol + threshold + unit;
            String amount = vo.getPayFee().floatValue() + "-" + vo.getMaxPayout().floatValue();

            String status = "";
            if (vo.getTriggerCheckState().intValue() == ConstantsOrder.TRIGGER_CHECK_STATE_YES) {
                if (null != vo.getPayoutFee() && 0 < vo.getPayoutFee().intValue()) {
                    status = "已判定，已履行";
                } else {
                    status = "已判定，无需履行";
                }
            } else {
                status = "等待判定";
            }

            AppOrderListVo appListVo = new AppOrderListVo();
            appListVo.setInnerOrderId(vo.getInnerOrderId());
            appListVo.setTitle(title);
            appListVo.setGmtCreate(vo.getGmtCreate());
            appListVo.setContent(content);
            appListVo.setAmount(amount);
            appListVo.setStatus(status);
            vos.add(appListVo);
        }
        return vos;
    }

    public static AppOrderInfoVo getAppVo(OrderInfoVo vo, OrderTrigger orderTrigger) throws Exception {
        String cityCnName = vo.getCityCnName();
        String weatherName = WeatherUtils.getSymbolChinese(vo.getWeatherType());
        String symbol = OpTypeUtils.getSymbol(vo.getOpType());
        String threshold = vo.getThreshold().toString();
        String unit = WeatherUtils.getUnit(vo.getWeatherType());
        String contractContent = weatherName + symbol + threshold + unit;
        String weatherContent = "";
        if (null != orderTrigger) {
            Float realWeather = orderTrigger.getNmcWeatherValue() != null ? orderTrigger.getNmcWeatherValue()
                    : orderTrigger.getCmaWeatherValue();
            weatherContent = weatherName + "为" + realWeather + unit;
        }

        String status = "";
        if (vo.getTriggerCheckState().intValue() == ConstantsOrder.TRIGGER_CHECK_STATE_YES) {
            if (null != vo.getPayoutFee() && 0 < vo.getPayoutFee().intValue()) {
                status = "已判定，已履行";
            } else {
                status = "已判定，无需履行";
            }
        } else {
            status = "等待判定";
        }

        AppOrderInfoVo appVo = new AppOrderInfoVo();
        appVo.setInnerOrderId(vo.getInnerOrderId());
        appVo.setGmtCreate(vo.getGmtCreate());
        appVo.setStatus(status);
        appVo.setCityName(cityCnName);
        appVo.setStime(vo.getStime().substring(0, 10));
        appVo.setContractContent(contractContent);
        appVo.setWeatherContent(weatherContent);
        appVo.setMaxPayout(vo.getMaxPayout());
        appVo.setOrderPrice(vo.getOrderPrice());
        appVo.setBuyCount(vo.getBuyCount());
        appVo.setPayFee(vo.getPayFee());

        return appVo;
    }

    // public static void main(String[] args) {
    // System.out.println("aabbcc".substring(0, 2));
    // }
}
