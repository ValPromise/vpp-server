package com.vpp.core.standardized.trigger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.vpp.common.filter.SendSmsUtil;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.ConstantsOrder;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.MD5Utils;
import com.vpp.core.customer.Customer;
import com.vpp.core.customer.CustomerMapper;
import com.vpp.core.standardized.order.IOrderService;
import com.vpp.core.standardized.order.OrderCity;
import com.vpp.core.standardized.order.OrderList;
import com.vpp.core.standardized.order.OrderListMapper;
import com.vpp.core.standardized.payout.OrderPayout;
import com.vpp.core.standardized.payout.OrderPayoutMapper;
import com.vpp.core.standardized.product.ProductConfig;
import com.vpp.core.standardized.product.ProductConfigMapper;

import net.sf.json.JSONObject;

@Service
public class TriggerService implements ITriggerService {
    private static final Logger logger = LogManager.getLogger(TriggerService.class);

    public static final String WEATHER_URL = "http://119.28.70.201:801/selfrain/getNmcCma";
    @Autowired
    public static final String TEMPLATE_101 = "101";
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderListMapper orderListMapper;
    @Autowired
    private OrderPayoutMapper orderPayoutMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductConfigMapper productConfigMapper;
    @Autowired
    private OrderTriggerMapper orderTriggerMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void triggerByEtime(String date) throws Exception {
        System.out.println(date);
        if (StringUtils.isBlank(date)) {
            date = DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN);
        }
        this.triggerByDate(date);
    }

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

    public void updateTrigger(List<OrderList> orderLists) throws Exception {
        List<OrderTrigger> orderTriggers = new ArrayList<OrderTrigger>();
        for (OrderList order : orderLists) {
            boolean dataIsNull = false;// 天气实况数据为空标识
            BigDecimal payoutFee = new BigDecimal(0);
            byte triggerState = 0;
            List<OrderCity> orderCitys = orderService.getOrderCityByInnerOrderId(order.getInnerOrderId());
            List<OrderTrigger> tempOrderTriggers = new ArrayList<OrderTrigger>();
            for (OrderCity orderCity : orderCitys) {
                String cityId = orderCity.getCityId();
                String date = DateUtil.format(orderCity.getStime(), DateUtil.YMD_DATE_TIME_PATTERN);
                Date time = new Date();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("city_id", cityId);
                params.put("date", date);
                params.put("token", MD5Utils.getMD5String(cityId + date + "tqb").toLowerCase());
                try {
                    String weatherStr = HttpUtils.post(WEATHER_URL, params);
                    if (null == weatherStr) {
                        logger.error("weather data is null {},{},{}", order.getInnerOrderId(),cityId, date);
                    }
                    JSONObject jsonObj = JSONObject.fromObject(weatherStr);
                    Float nmcVal = null;
                    Float cmaVal = null;
                    switch (orderCity.getWeatherType()) {
                    case 1:
                        nmcVal = Float.valueOf(jsonObj.get("nmc_max_temp").toString());
                        cmaVal = Float.valueOf(jsonObj.get("cma_max_temp").toString());
                        break;
                    case 4:
                        nmcVal = Float.valueOf(jsonObj.get("nmc_prcp").toString());
                        cmaVal = Float.valueOf(jsonObj.get("cma_prcp").toString());
                        break;
                    default:
                        break;
                    }
                    OrderTrigger orderTrigger = new OrderTrigger();
                    String triggerCode = DealUtil.createId("");
                    if (nmcVal != null || cmaVal != null) {
                        Float realVal = nmcVal != null ? nmcVal : cmaVal;
                        boolean isTrigger = DealUtil.weatherCompare(realVal, orderCity.getThreshold(), orderCity.getOpType());
                        if (isTrigger) {
                            triggerState = 1;
                            payoutFee = order.getMaxPayout();
                        }
                        orderTrigger.setCityId(cityId);
                        orderTrigger.setCmaWeatherValue(cmaVal);
                        orderTrigger.setDataState((byte) 1);
                        orderTrigger.setGmtCreate(new Date());
                        orderTrigger.setInnerOrderId(order.getInnerOrderId());
                        orderTrigger.setNmcWeatherValue(nmcVal);
                        orderTrigger.setPayoutFee(payoutFee);
                        orderTrigger.setRealWeatherDate(orderCity.getStime());
                        orderTrigger.setThreshold(orderCity.getThreshold());
                        orderTrigger.setTriggerState(triggerState);
                        tempOrderTriggers.add(orderTrigger);
                    } else {
                        continue;
                    }
                    if (!dataIsNull) {
                        // 天气实况数据
                        orderTriggerMapper.insertSelective(orderTrigger);
                        // 修改触发数据，赔付数据为异常重复触发
                        orderListMapper.updateOrderTriggerByInnerOrderId(order.getInnerOrderId(), payoutFee,
                                ConstantsOrder.IS_OVER_TRIGGER_YES);
                        Customer customer = customerMapper.selectByPrimaryKey(order.getCustomerId());
                        ProductConfig productConfig = productConfigMapper.selectByProductId(order.getProductId());
                        // 判断是否需要赔付
                        if (DealUtil.priceCompare(payoutFee, new BigDecimal(0), ">")) {
                            OrderPayout payout = new OrderPayout();
                            payout.setCustomerId(order.getCustomerId());
                            payout.setDataState((byte) 1);
                            payout.setGmtCreate(new Date());
                            payout.setInnerOrderId(order.getInnerOrderId());
                            payout.setPayoutState((byte) 0);
                            payout.setPayoutFee(payoutFee);
                            payout.setTriggerCode(triggerCode);
                            // 触发赔付短信 2018-05-17 start ----
                            SendSmsUtil.sendSmsTrigger(customer.getMobile(), order.getInnerOrderId(),
                                    productConfig.getProductName(), Integer.valueOf(payoutFee.intValue()));
                            // 触发赔付短信 2018-05-17 end ----
                            orderPayoutMapper.insertSelective(payout);
                        } else {
                            // 未触发赔付短信 2018-05-17 start ----
                            SendSmsUtil.sendSmsNoTrigger(customer.getMobile(), order.getInnerOrderId(),
                                    productConfig.getProductName());
                            // 未触发赔付短信 2018-05-17 end ----
                        }
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    continue;
                }
            }
        }
    }

    @Override
    public OrderTrigger getTriggerByInnerOrderId(String innerOrderId) {
        return orderTriggerMapper.getTriggerByInnerOrderId(innerOrderId);
    }

}
