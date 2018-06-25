package com.vpp.core.app.temp.controller;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.google.gson.Gson;
import com.vpp.common.utils.AppUtils;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.ConstantsTemp;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.TempOrderBeanUtils;
import com.vpp.common.vo.AppOrderInfoVo;
import com.vpp.common.vo.AppOrderListVo;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;
import com.vpp.core.standardized.order.service.IContractService;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.pay.IPayService;
import com.vpp.core.standardized.trigger.ITriggerService;
import com.vpp.core.standardized.trigger.OrderTrigger;

@RestController
@RequestMapping("/app/temp/order")
public class AppTempOrderController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppTempOrderController.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate<String, String> searchRedis;
    @Autowired
    private IContractService contractService;
    @Autowired
    private RedisTemplate<String, String> buyRedis;
    @Autowired
    private IPayService payService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ITriggerService triggerService;

    public static final String SALEINFO_VPP_PRE = "saleinfo_vpp_";
    public static final String PRODUCT_ID = "";

    public static final Integer STATE_SUCCESS = 1;
    public static final Integer STATE_ERROR = 0;
    public static final String CACHE_ORDER_CITY = "cache_roder_city_";

    @RequestMapping(value = "/addOrder")
    public synchronized ResultVo addOrder(String token, String contractId, Float strike, Integer buyCnt,
            HttpServletResponse response) {
        logger.info("token==" + token + "  合约Id==" + contractId + "  触发值==" + strike + "购买份数==" + buyCnt);
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        Long customerId = Long.valueOf(getCustomerIdByToken(token));
        if (null == customerId) {
            return ResultVo.setResultError(getMessage("token")); // 无效token
        }
        Gson gson = new Gson();
        if (StringUtils.isBlank(contractId) || null == strike || buyCnt == null) {
            return ResultVo.setResultError(getMessage("required_is_not_null"));
        }
        // 限制购买份数
        if (buyCnt > ConstantsTemp.MAX_BUY_COUNT) {
            return ResultVo.setResultError(MessageFormat.format(getMessage("once_max_buy_count"), 3));
        }
        // 查询合约是不是存在
        if (!searchRedis.opsForHash().hasKey(contractId, strike.toString())) {
            // 合约不存在
            return ResultVo.setResultError(getMessage("contract_timeout"));
        }

        String searchObjStr = (String) searchRedis.opsForHash().get(contractId, strike.toString());
        Map<String, String> searchObj = gson.fromJson(searchObjStr, Map.class);
        String cityId = searchObj.get("cityId").toString();

        // 风控： 1：单用户单城市上限3单， 2：单产品单日购买上限100单
        String roskResult = this.risk(ConstantsTemp.PRODUCT_ID, customerId, cityId);
        if (null != roskResult) {
            return ResultVo.setResultError(roskResult);
        }

        OrderList orderlist = orderService.getOrderByOrderId(contractId);
        if (orderlist != null) {
            logger.info("订单号已存在");
            return ResultVo.setResultError(getMessage("add_order_error"));
        } else {
            Customer customer = customerService.selectCustomerById(customerId);

            OrderList order = TempOrderBeanUtils.getOrderList(searchObj, buyCnt, customerId, contractId);
            OrderCity city = TempOrderBeanUtils.getOrderCity(searchObj, contractId);
            // 判断客户账户余额是否可以支付
            if (!cheackCustomerBlance(customer, order.getOrderPrice())) {
                return ResultVo.setResultError(getMessage("not_sufficient_funds"));
            }
            // 客户钱包金额 减 本次支付金额
            customer.setBalance(DealUtil.priceSubtract(customer.getBalance(), order.getOrderPrice()));

            try {
                orderService.tempContractPay(order, city, customer);

                return ResultVo.setResultSuccess(getMessage("add_order_success"));
            } catch (Exception e) {
                logger.error("支付失败 error:::{}", e.getMessage());
                return ResultVo.setResultError(getMessage("add_order_error"));// 系统繁忙
            }
        }
    }

    @RequestMapping(value = "/getOrderList")
    public ResultVo getOrderList(String token, Integer currentPage, Integer pageSize, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        currentPage = null == currentPage ? 1 : currentPage;
        pageSize = null == pageSize ? 15 : pageSize;

        // 根据token查询登录用户customerId
        String customerId = this.getCustomerIdByToken(token);
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("state", ConstantsServer.STATE_ENABLE);

            Page<OrderInfoVo> tempOrders = orderService.getSuccessOrdersByCondition(currentPage, pageSize,
                    Long.valueOf(customerId), ConstantsTemp.PRODUCT_ID);

            List<AppOrderListVo> tempAppOrders = AppUtils.getAppVoList(tempOrders);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", tempAppOrders);
            result.put("total", tempOrders.getTotal());
            result.put("currentPage", tempOrders.getPageNum());
            result.put("pageSize", tempOrders.getPageSize());
            return ResultVo.setResultSuccess(result);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }

    }

    @RequestMapping(value = "/getOrderInfo")
    public ResultVo getOrderInfo(String token, String innerOrderId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        try {
            OrderInfoVo orderInfoVo = orderService.findOrderInfoVoByInnerOrderId(innerOrderId);
            OrderTrigger orderTrigger = triggerService.getTriggerByInnerOrderId(innerOrderId);
            AppOrderInfoVo appOrderInfoVo = AppUtils.getAppVo(orderInfoVo, orderTrigger);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("bean", appOrderInfoVo);
            result.put("weatherUrl", ConstantsServer.WEATHER_HELP_URL);
            return ResultVo.setResultSuccess(result);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }
    }

    /**
     * 对比余额
     * 
     * @author cgp
     * @param customer
     * @param orderPrice
     * @return
     */
    private boolean cheackCustomerBlance(Customer customer, BigDecimal orderPrice) {
        logger.info(DateUtil.format(new Date(), DateUtil.LONG_DATE_TIME_PATTERN) + "用户mobile ===" + customer.getMobile()
                + " 用户ID===" + customer.getId() + " 用户余额=== " + customer.getBalance() + " ===订单金额== " + orderPrice);
        if (DealUtil.priceCompare(customer.getBalance(), orderPrice, "<")) {
            return false;
        }
        return true;
    }

    /**
     * 风控
     * 
     * @author Lxl
     * @param productId
     * @param customerId
     * @param cityId
     * @return
     */
    private String risk(String productId, Long customerId, String cityId) {
        String date = DateUtil.getCurrentDateTimeString(DateUtil.YMD_DATE_PATTERN);
        // 风控： 1：单用户单城市上限3单， 2：单产品单城市单日购买上限100单
        try {

            Integer countByProductId = orderService.fingRiskCountByProductId(productId, cityId, date);
            if (countByProductId >= ConstantsTemp.RISK_PRODUCT_DATE_COUNT) {
                return getMessage("risk_product_date_count");
            }

            String dateh = DateUtil.getCurrentDateTimeString(DateUtil.YMDH_DATE_PATTERN);
            Integer countByHh = orderService.fingRiskCountByProductId(productId, cityId, dateh);
            if (countByHh >= ConstantsTemp.RISK_PRODUCT_DATE_HOUR_COUNT) {
                return getMessage("risk_product_date_hour_count");
            }

            Integer countByCustomerId = orderService.fingRiskCountByCustomerId(productId, customerId, date);
            if (countByCustomerId >= ConstantsTemp.RISK_CUSTOMER_DATE_COUNT) {
                return MessageFormat.format(getMessage("risk_customer_date_count"), ConstantsTemp.RISK_CUSTOMER_DATE_COUNT);
            }

            Integer countByCustomerIdCityId = orderService.fingRiskCountByCustomerIdCityId(productId, customerId, cityId, date);
            if (countByCustomerIdCityId >= ConstantsTemp.RISK_CUSTOMER_CITY_DATE_COUNT) {
                return MessageFormat.format(getMessage("risk_customer_city_date_count"),
                        ConstantsTemp.RISK_CUSTOMER_CITY_DATE_COUNT);
            }
        } catch (Exception e) {
        }

        return null;
    }
}
