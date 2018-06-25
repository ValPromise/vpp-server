package com.vpp.core.rain.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.ConstantsOrder;
import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.rain.service.IRainOrderTriggerService;
import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;
import com.vpp.core.standardized.order.mapper.OrderListMapper;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.payout.OrderPayout;
import com.vpp.core.standardized.payout.OrderPayoutMapper;
import com.vpp.core.standardized.trigger.OrderTrigger;
import com.vpp.core.standardized.trigger.OrderTriggerMapper;
import com.vpp.core.weather.bean.WeatherData;
import com.vpp.core.weather.service.IWeatherService;

@Service
public class RainOrderTriggerService implements IRainOrderTriggerService {
    private static final Logger logger = LogManager.getLogger(RainOrderTriggerService.class);

    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderListMapper orderListMapper;
    @Autowired
    private OrderPayoutMapper orderPayoutMapper;
    @Autowired
    private OrderTriggerMapper orderTriggerMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private IWeatherService weatherService;
    @Autowired
    private ICustomerCashLogService customerCashLogService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void triggerByEtime(String date) throws Exception {
        if (StringUtils.isBlank(date)) {
            date = DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN);
        }
        this.triggerByDate(date);
    }

    // @Transactional( propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void triggerByDate(String etime) throws Exception {
        int currentPage = 1;
        int totalPage = 0;// 总页数
        int limit = 200;// 每页行数限制
        do {
            // 查询etime以前的未触发已支付的正常订单
            Page<OrderList> list = orderService.findLimitByTemplateIdEtime(currentPage, limit, ConstantsRain.TEMPLATE_ID,
                    etime);
            // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
            PageInfo<OrderList> pageInfo = new PageInfo<>(list);
            if (totalPage == 0) {// 取一次总页数
                totalPage = pageInfo.getPages();
            }
            this.updateTrigger(pageInfo.getList());// 触发判断
            currentPage++;
        } while (currentPage <= totalPage);
    }

    // @Transactional( propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateTrigger(List<OrderList> orderLists) throws Exception {
        List<OrderTrigger> orderTriggers = new ArrayList<OrderTrigger>();
        for (OrderList order : orderLists) {
            BigDecimal payoutFee = new BigDecimal(0);
            byte triggerState = 0;
            List<OrderCity> orderCitys = orderService.getOrderCityByInnerOrderId(order.getInnerOrderId());
            // List<OrderTrigger> tempOrderTriggers = new ArrayList<OrderTrigger>();
            for (OrderCity orderCity : orderCitys) {
                String cityId = orderCity.getCityId();
                String date = orderCity.getStime();
                date = DateUtil.stringToString(date, DateUtil.LONG_DATE_TIME_PATTERN, DateUtil.YMD_DATE_TIME_PATTERN);
                WeatherData weatherData = weatherService.selectWeatherTemp(cityId, date);
                // WeatherData weatherData = this.getTestWeather();
                if (null == weatherData || null == weatherData.getRealPrcp()) {
                    logger.error("weather data is null {},{},{}", order.getInnerOrderId(), cityId, date);
                    continue;
                }

                boolean isTrigger = DealUtil.weatherCompare(weatherData.getRealPrcp(), orderCity.getThreshold(),
                        orderCity.getOpType());
                if (isTrigger) {
                    triggerState = 1;
                    payoutFee = order.getMaxPayout();
                }

                OrderTrigger orderTrigger = new OrderTrigger();
                orderTrigger.setCityId(cityId);
                orderTrigger.setCmaWeatherValue(weatherData.getCmaPrcp());
                orderTrigger.setNmcWeatherValue(weatherData.getNmcPrcp());
                orderTrigger.setDataState(ConstantsServer.STATE_ENABLE);
                orderTrigger.setGmtCreate(DateUtil.getCurrentDateTimeLocal());
                orderTrigger.setInnerOrderId(order.getInnerOrderId());
                orderTrigger.setPayoutFee(payoutFee);
                orderTrigger.setRealWeatherDate(orderCity.getStime());
                orderTrigger.setThreshold(orderCity.getThreshold());
                orderTrigger.setTriggerState(triggerState);
                orderTriggers.add(orderTrigger);

                // 修改触发数据，赔付数据为异常重复触发
                orderListMapper.updateTriggerSuccessByInnerOrderId(order.getInnerOrderId(), payoutFee, payoutFee,
                        ConstantsOrder.IS_OVER_TRIGGER_YES);
                if (DealUtil.priceCompare(payoutFee, new BigDecimal(0), ">")) {
                    // 判断是否需要赔付
                    OrderPayout payout = new OrderPayout();
                    payout.setCustomerId(order.getCustomerId());
                    payout.setDataState(ConstantsServer.STATE_ENABLE);
                    payout.setGmtCreate(new Date());
                    payout.setInnerOrderId(order.getInnerOrderId());
                    payout.setPayoutState(ConstantsOrder.PAYOUT_STATE_YES);
                    payout.setPayoutFee(payoutFee);
                    payout.setRealPayoutFee(payoutFee);
                    String triggerCode = DealUtil.createId("T");
                    payout.setTriggerCode(triggerCode);
                    orderPayoutMapper.insertSelective(payout);

                    // 修改客户钱包余额
                    customerMapper.incomeBalance(order.getCustomerId(), payoutFee);
                    customerCashLogService.insertPayout(order.getCustomerId(), payoutFee, "降水产品赔付" + order.getInnerOrderId());
                }
            }
        }
        if (!orderTriggers.isEmpty()) {
            // 批量录入触发数据
            orderTriggerMapper.insertOrderTriggerList(orderTriggers);
        }
    }

    private WeatherData getTestWeather() {
        WeatherData weatherData = new WeatherData();
        weatherData.setCmaMaxTemp(40f);
        weatherData.setNmcMaxTemp(41f);

        weatherData.setCmaPrcp(22f);
        weatherData.setNmcPrcp(23f);
        return weatherData;
    }
}
