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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class AppUtils {

    @Autowired
    private MessageSourceUtils messageSourceUtils;

    /**
     * 封装app合约列表对象
     * 
     * @author Lxl
     * @param orders
     * @return
     * @throws Exception
     */
    public List<AppOrderListVo> getAppVoList(List<OrderInfoVo> orders) throws Exception {
        List<AppOrderListVo> vos = new ArrayList<AppOrderListVo>();
        for (OrderInfoVo vo : orders) {

            String cityName = this.getCityNameI18n(vo);
            String st = DateUtil.strToStr(vo.getStime(), DateUtil.LONG_DATE_TIME_PATTERN, "MMdd");

            String weatherName = WeatherUtils.getSymbolI18n(vo.getWeatherType());
            String symbol = OpTypeUtils.getSymbol(vo.getOpType());
            String threshold = vo.getThreshold().toString();
            String unit = WeatherUtils.getUnit(vo.getWeatherType());

            String title = cityName + st;
            String content = weatherName + symbol + threshold + unit;
            String amount = vo.getPayFee().floatValue() + "-" + vo.getMaxPayout().floatValue();

            AppOrderListVo appListVo = new AppOrderListVo();
            this.putTriggerStateI18nList(vo, appListVo);

            appListVo.setInnerOrderId(vo.getInnerOrderId());
            appListVo.setTitle(title);
            appListVo.setGmtCreate(vo.getGmtCreate());
            appListVo.setContent(content);
            appListVo.setAmount(amount);
            vos.add(appListVo);
        }
        return vos;
    }

    public AppOrderInfoVo getAppVo(OrderInfoVo vo, OrderTrigger orderTrigger) throws Exception {
        String cityName = this.getCityNameI18n(vo);
        String weatherName = WeatherUtils.getSymbolI18n(vo.getWeatherType());
        String symbol = OpTypeUtils.getSymbol(vo.getOpType());
        String threshold = vo.getThreshold().toString();
        String unit = WeatherUtils.getUnit(vo.getWeatherType());
        String contractContent = weatherName + symbol + threshold + unit;
        String weatherContent = "";
        if (null != orderTrigger) {
            Float realWeather = orderTrigger.getNmcWeatherValue() != null ? orderTrigger.getNmcWeatherValue()
                    : orderTrigger.getCmaWeatherValue();
            weatherContent = weatherName + " " + realWeather + unit;
        }
        // 状态 0：等待判定，1：输，2：赢，3：退款

        AppOrderInfoVo appVo = new AppOrderInfoVo();
        this.putTriggerStateI18n(vo, appVo);

        appVo.setInnerOrderId(vo.getInnerOrderId());
        appVo.setGmtCreate(vo.getGmtCreate());
        appVo.setCityName(cityName);
        appVo.setStime(vo.getStime().substring(0, 10));
        appVo.setContractContent(contractContent);
        appVo.setWeatherContent(weatherContent);
        appVo.setMaxPayout(vo.getMaxPayout());
        appVo.setOrderPrice(vo.getOrderPrice());
        appVo.setBuyCount(vo.getBuyCount());
        appVo.setPayFee(vo.getPayFee());

        return appVo;
    }

    /**
     * 订单状态 0：等待判定
     */
    public static final Byte ORDER_STATUS_PENDING = 0;
    /**
     * 订单状态 1：输
     */
    public static final Byte ORDER_STATUS_LOSE = 1;
    /**
     * 订单状态 2：赢
     */
    public static final Byte ORDER_STATUS_WIN = 2;
    /**
     * 订单状态 3：退款
     */
    public static final Byte ORDER_STATUS_REFUND = 3;

    private void putTriggerStateI18n(OrderInfoVo vo, AppOrderInfoVo appVo) {
        String statusString = "";
        String status = "";
        if (vo.getTriggerCheckState().intValue() == ConstantsOrder.TRIGGER_CHECK_STATE_YES) {
            if (null != vo.getPayoutFee() && 0 < vo.getPayoutFee().intValue()) {
                statusString = messageSourceUtils.getMessage("trigger_status_success_payment");
                status = ORDER_STATUS_WIN.toString();
            } else {
                statusString = messageSourceUtils.getMessage("trigger_status_success_nopayment");
                status = ORDER_STATUS_LOSE.toString();
            }
        } else {
            statusString = messageSourceUtils.getMessage("trigger_status_pending");
            status = ORDER_STATUS_PENDING.toString();
        }
        appVo.setStatus(status);
        appVo.setStatusString(statusString);
    }

    private void putTriggerStateI18nList(OrderInfoVo vo, AppOrderListVo appVo) {
        String statusString = "";
        String status = "";
        if (vo.getTriggerCheckState().intValue() == ConstantsOrder.TRIGGER_CHECK_STATE_YES) {
            if (null != vo.getPayoutFee() && 0 < vo.getPayoutFee().intValue()) {
                statusString = messageSourceUtils.getMessage("trigger_status_success_payment");
                status = ORDER_STATUS_WIN.toString();
            } else {
                statusString = messageSourceUtils.getMessage("trigger_status_success_nopayment");
                status = ORDER_STATUS_LOSE.toString();
            }
        } else {
            statusString = messageSourceUtils.getMessage("trigger_status_pending");
            status = ORDER_STATUS_PENDING.toString();
        }
        appVo.setStatus(status);
        appVo.setStatusString(statusString);
    }

    public String getCityNameI18n(OrderInfoVo vo) {
        if (LocaleUtils.isEn()) {
            return vo.getCityEnName();
        } else {
            return vo.getCityCnName();
        }
    }
}
