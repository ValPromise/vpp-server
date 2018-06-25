package com.vpp.core.standardized.trigger;

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
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;
import com.vpp.core.standardized.order.mapper.OrderListMapper;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.payout.OrderPayout;
import com.vpp.core.standardized.payout.OrderPayoutMapper;
import com.vpp.core.weather.bean.WeatherData;
import com.vpp.core.weather.service.IWeatherService;

@Service
public class TriggerService implements ITriggerService {
    private static final Logger logger = LogManager.getLogger(TriggerService.class);

    @Autowired
    public static final String TEMPLATE_101 = "101";
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void triggerByDate(String etime) throws Exception {
        int currentPage = 1;
        int totalPage = 0;// 总页数
        int limit = 200;// 每页行数限制
        do {
            Page<OrderList> list = orderService.findLimitByTemplateIdEtime(currentPage, limit, TEMPLATE_101, etime);
            // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
            PageInfo<OrderList> pageInfo = new PageInfo<>(list);
            if (totalPage == 0) {// 取一次总页数
                totalPage = pageInfo.getPages();
            }
            this.updateTrigger(pageInfo.getList());// 触发判断
            currentPage++;
        } while (currentPage <= totalPage);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                try {
                    WeatherData weatherData = weatherService.selectWeatherTemp(cityId, date);
                    if (null == weatherData || null == weatherData.getRealMaxTemp()) {
                        logger.error("weather data is null {},{},{}", order.getInnerOrderId(), cityId, date);
                        continue;
                    }

                    OrderTrigger orderTrigger = new OrderTrigger();

                    boolean isTrigger = DealUtil.weatherCompare(weatherData.getRealMaxTemp(), orderCity.getThreshold(),
                            orderCity.getOpType());
                    if (isTrigger) {
                        triggerState = 1;
                        payoutFee = order.getMaxPayout();
                    }
                    orderTrigger.setCityId(cityId);
                    orderTrigger.setCmaWeatherValue(weatherData.getCmaMaxTemp());
                    orderTrigger.setDataState((byte) 1);
                    orderTrigger.setGmtCreate(DateUtil.getCurrentDateTimeLocal());
                    orderTrigger.setInnerOrderId(order.getInnerOrderId());
                    orderTrigger.setNmcWeatherValue(weatherData.getNmcMaxTemp());
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
                        customerCashLogService.insertPayout(order.getCustomerId(), payoutFee,
                                "高温产品赔付" + order.getInnerOrderId());
                    }

                    // // 修改触发数据，赔付数据为异常重复触发
                    // orderListMapper.updateOrderTriggerByInnerOrderId(order.getInnerOrderId(), payoutFee,
                    // ConstantsOrder.IS_OVER_TRIGGER_YES);
                    // // 判断是否需要赔付
                    // if (DealUtil.priceCompare(payoutFee, new BigDecimal(0), ">")) {
                    // OrderPayout payout = new OrderPayout();
                    // payout.setCustomerId(order.getCustomerId());
                    // payout.setDataState((byte) 1);
                    // payout.setGmtCreate(new Date());
                    // payout.setInnerOrderId(order.getInnerOrderId());
                    // payout.setPayoutState((byte) 0);
                    // payout.setPayoutFee(payoutFee);
                    // payout.setTriggerCode(triggerCode);
                    // orderPayoutMapper.insertSelective(payout);
                    // }
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    continue;
                }
            }
        }
        if (!orderTriggers.isEmpty()) {
            // 批量录入触发数据
            orderTriggerMapper.insertOrderTriggerList(orderTriggers);
        }
    }

    @Override
    public OrderTrigger getTriggerByInnerOrderId(String innerOrderId) {
        return orderTriggerMapper.getTriggerByInnerOrderId(innerOrderId);
    }

}
