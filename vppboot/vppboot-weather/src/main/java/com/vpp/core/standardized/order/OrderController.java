package com.vpp.core.standardized.order;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import com.vpp.common.filter.SendSmsUtil;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.ConstantsOrder;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.Customer;
import com.vpp.core.customer.ICustomerService;
import com.vpp.core.standardized.pay.IPayService;
import com.vpp.core.standardized.product.IProductConfigService;
import com.vpp.core.standardized.product.ProductConfig;
import com.vpp.core.standardized.trigger.ITriggerService;
import com.vpp.core.standardized.trigger.OrderTrigger;
import com.vpp.vo.OrderInfoVo;
import com.vpp.vo.ResultVo;

@RestController
@RequestMapping("/order")
public class OrderController extends CommonController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate<String, String> searchRedis;
    @Autowired
    private IProductConfigService productConfigService;
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

    public static final Byte OP_TYPE = 5;

    public static final Byte WEATHER_TYPE = 1;

    private static final int TRIGGER_DATE_ADD = 2;

    /**
     * 单用户单城市单日购买数量上限 3
     */
    private static final int RISK_CUSTOMER_CITY_DATE_COUNT = 3;

    /**
     * 单用户单日购买数量上限10
     */
    private static final int RISK_CUSTOMER_DATE_COUNT = 10;

    /**
     * 单产品单日购买上限 100
     */
    private static final int RISK_PRODUCT_DATE_COUNT = 100;

    @RequestMapping(value = "/addOrder")
    public synchronized ResultVo addOrder(String token, String contractId, String strike, Integer buyCnt, String mid,
            HttpServletResponse response) throws Exception {
        logger.info("token==" + token + "  合约Id==" + contractId + "  触发值==" + strike + "购买份数==" + buyCnt);
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        Long customerId = Long.valueOf(getTokenId(token));
        Gson gson = new Gson();
        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(strike) || buyCnt == null) {
            return ResultVo.setResultError(getMessage("required_is_not_null"));
        }
        mid = URLDecoder.decode(mid, "UTF-8");// 转码
        // 查询合约是不是存在
        String searchObjStr = (String) searchRedis.opsForHash().get(contractId, strike);
        if (StringUtils.isBlank(searchObjStr)) {
            // 合约不存在
            return ResultVo.setResultError(getMessage("contract_is_null"));
        }
        // 限制购买份数
        if (buyCnt > 3) {
            return ResultVo.setResultError(MessageFormat.format(getMessage("risk_customer_city_date_count"), 3));
        }

        Map<String, String> searchObj = gson.fromJson(searchObjStr, Map.class);
        String cityId = searchObj.get("cityId").toString();

        // 风控： 1：单用户单城市上限3单， 2：单产品单日购买上限100单
        String roskResult = this.risk(ConstantsOrder.Product.PRODUCT_ID_TEMP, customerId, cityId);
        if (null != roskResult) {
            return ResultVo.setResultError(roskResult);
        }

        if (DateUtil.diffDateTimeMin(new Date(),
                DateUtil.parse(searchObj.get("searchDate"), DateUtil.LONG_DATE_TIME_PATTERN_SSS)) > 3) {
            return ResultVo.setResultError(getMessage("contract_timeout"));
        }
        OrderList orderlist = orderService.getOrderByOrderId(contractId);
        if (orderlist != null) {
            logger.info("订单号已存在");
            return ResultVo.setResultError(getMessage("add_order_error"));
        } else {
            Customer customer = customerService.selectCustomerById(customerId);
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
            order.setMaxPayout(priceMultiplyBuyCnt(formtPrice(payoff), buyCnt));
            order.setOpType(OP_TYPE);
            order.setWeatherType(WEATHER_TYPE);
            order.setTemplateId(searchObj.get("template_id").toString());
            order.setOrderPrice(priceMultiplyBuyCnt(formtPrice(price), buyCnt));
            order.setTriggerRuleParam(threshold);
            order.setOrderState((byte) 1);
            order.setStime(stime);
            order.setEtime(etime);
            order.setTriggerCheckState((byte) 0);
            order.setProductId(ConstantsOrder.Product.PRODUCT_ID_TEMP);

            ProductConfig productConfig = productConfigService.selectProductByProductId(ConstantsOrder.Product.PRODUCT_ID_TEMP);
            OrderCity city = new OrderCity();
            city.setInnerOrderId(contractId);
            city.setCityId(cityId);
            city.setThreshold(Float.valueOf(threshold));
            city.setStime(stime);
            city.setEtime(etime);
            city.setOpType(OP_TYPE);
            city.setWeatherType(WEATHER_TYPE);
            city.setContractId(contractId);
            city.setContractPrice(formtPrice(price));
            city.setPayoutRuleParam(payoff);
            // 支付

            if (!cheackCustomerBlance(customer, order.getOrderPrice())) {
                return ResultVo.setResultError(getMessage("not_sufficient_funds"));
            }
            // 设置支付信息
            order.setPayState((byte) 1);
            customer.setBalance(DealUtil.priceSubtract(customer.getBalance(), order.getOrderPrice()));
            order.setPayState((byte) 1);
            order.setPayTime(new Date());
            order.setPayFee(order.getOrderPrice());
            boolean result = this.pay(order, city, customer);
            if (!result) {
                logger.info("支付失败");
                return ResultVo.setResultError(getMessage("add_order_error"));// 系统繁忙
            } else {
                String saleCacheKey = SALEINFO_VPP_PRE + cityId + "_"
                        + DateUtil.strToStr(time, DateUtil.YMD_DATE_PATTERN, DateUtil.YMD_DATE_TIME_PATTERN);
                // Map<String, Object> returnObj = new HashMap<String, Object>();

                // returnObj.put("innerOrderId", order.getInnerOrderId());
                // returnObj.put("totalFee", order.getOrderPrice());
                // returnObj.put("gmtCreate", DateUtil.getMillis(order.getGmtCreate()));
                // returnObj.put("immediatePay", 1);
                String saleCntStr = (String) buyRedis.opsForHash().get(saleCacheKey, strike);
                int saleCnt = StringUtils.isBlank(saleCntStr) ? 0 : Integer.valueOf(saleCntStr);
                buyRedis.opsForHash().put(saleCacheKey, strike, String.valueOf(saleCnt + buyCnt));
                // 发送短信 2018-05-17 start -----
                String cityName = contractService.getCityNameByCityId(cityId);
                String contractDate = DateUtil.format(stime, DateUtil.YMD_DATE_PATTERN);
                String triggerDate = DateUtil.format(DateUtil.addDate(stime, TRIGGER_DATE_ADD), DateUtil.YMD_DATE_PATTERN);// T+2触发
                SendSmsUtil.sendSmsBuySuccess(customer.getMobile(), order.getInnerOrderId(), productConfig.getProductName(),
                        contractDate, triggerDate);
                // 发送短信 2018-05-17 end ---------
                return ResultVo.setResultSuccess(getMessage("add_order_success"));
            }
        }

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
            Integer countByCustomerIdCityId = orderService.fingRiskCountByCustomerIdCityId(productId, customerId, cityId, date);
            if (countByCustomerIdCityId >= RISK_CUSTOMER_CITY_DATE_COUNT) {
                return MessageFormat.format(getMessage("risk_customer_city_date_count"), RISK_CUSTOMER_CITY_DATE_COUNT);
            }
            Integer countByCustomerId = orderService.fingRiskCountByCustomerId(productId, customerId, date);
            if (countByCustomerId >= RISK_CUSTOMER_DATE_COUNT) {
                return MessageFormat.format(getMessage("risk_customer_date_count"), RISK_CUSTOMER_DATE_COUNT);
            }
            Integer countByProductId = orderService.fingRiskCountByProductId(productId, cityId, date);
            if (countByProductId >= RISK_PRODUCT_DATE_COUNT) {
                return getMessage("risk_product_date_count");
            }
        } catch (Exception e) {
        }

        return null;
    }

    // /备用
    // @RequestMapping(value = "/getOrderList")
    // public ResultVo getOrderList(String token, Integer currentPage, Integer pageSize, String innerOrderId, String stime,
    // String etime, HttpServletResponse response) {
    // response.addHeader("Access-Control-Allow-Origin", "*");
    // if (!checkLogin(token)) {
    //
    // return ResultVo.setResultError("登录失败");
    // }
    // if (null == currentPage) {
    // currentPage = new Integer(1);
    // }
    // if (null == pageSize) {
    // pageSize = new Integer(15);
    // }
    // if (!StringUtils.isNotBlank(stime)) {
    // stime = DateUtil.format(DateUtil.addDate(new Date(), -30), DateUtil.YMD_DATE_PATTERN);
    // }
    // if (!StringUtils.isNotBlank(etime)) {
    // etime = DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN);
    // }
    // Long customerId = Long.valueOf(getTokenId(token));
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("customerId", customerId);
    // params.put("stime", stime);// 开始时间
    // params.put("etime", etime);// 结束时间
    // Page<OrderVo> list = orderService.getOrderList(currentPage, pageSize, params);
    // PageInfo<OrderVo> pageInfos = new PageInfo<OrderVo>(list);
    // List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
    // for (OrderVo orderVo : pageInfos.getList()) {
    // OrderList order = orderVo.getOrderList();
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("innerOrderId", order.getInnerOrderId());
    // map.put("buyTime", order.getGmtCreate());
    // map.put("buyCount", order.getBuyCount());
    // map.put("payFee", order.getPayFee());
    // map.put("payTime", order.getPayTime());
    // map.put("stime", order.getStime());
    // map.put("etime", order.getEtime());
    // map.put("opType", order.getOpType());
    // map.put("weatherType", order.getWeatherType());
    // map.put("productName", productConfigService.getProductNameByProductId(order.getProductId()));
    // List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
    // List<OrderCity> citys = orderVo.getOrderCitys();
    // for (OrderCity orderCity : citys) {
    // Map<String, Object> cityMap = new HashMap<String, Object>();
    // cityMap.put("threshold", orderCity.getThreshold());
    // cityMap.put("cityId", orderCity.getCityId());
    // cityMap.put("cityName", contractService.getCityNameByCityId(orderCity.getCityId()));
    // cityList.add(cityMap);
    // }
    // map.put("orderCitys", cityList);
    // retList.add(map);
    // }
    // Map<String, Object> result = new HashMap<String, Object>();
    // result.put("rows", retList);
    // result.put("total", list.getTotal());
    // result.put("currentPage", list.getPageNum());
    // return ResultVo.setResultSuccess(result);
    // }
    @RequestMapping(value = "/getOrderList")
    public ResultVo getOrderList(String token, Integer currentPage, Integer pageSize, String innerOrderId, String stime,
            String etime, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        if (null == currentPage) {
            currentPage = new Integer(1);
        }
        if (null == pageSize) {
            pageSize = new Integer(15);
        }
        if (!StringUtils.isNotBlank(stime)) {
            stime = DateUtil.format(DateUtil.addDate(new Date(), -30), DateUtil.YMD_DATE_PATTERN);
        }
        if (!StringUtils.isNotBlank(etime)) {
            etime = DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN);
        }
        Long customerId = Long.valueOf(getTokenId(token));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customerId", customerId);
        params.put("stime", stime);// 开始时间
        params.put("etime", etime);// 结束时间
        Page<OrderInfoVo> list = orderService.selectOrderList(currentPage, pageSize, params);
        PageInfo<OrderInfoVo> pageInfos = new PageInfo<OrderInfoVo>(list);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (OrderInfoVo order : pageInfos.getList()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("innerOrderId", order.getInnerOrderId());
            map.put("productName",
                    contractService.getCityNameByCityId(order.getCityId()) + DateUtil.format(order.getStime(), "MMdd"));
            map.put("payoutFee", order.getPayoutFee() == null ? 0 : order.getPayoutFee());
            String condition = DealUtil.getWeatherTypeStr(order.getWeatherType()) + DealUtil.getOpTypeStr(order.getOpType())
                    + order.getThreshold() + DealUtil.getWeatherTypeSuffix(order.getWeatherType());
            map.put("condition", condition);
            String triggerState = "";
            int isChangeColor = 0;
            if (order.getTriggerCheckState().equals((byte) 1)
                    && DealUtil.priceCompare(order.getRealPayoutFee(), new BigDecimal(0), ">")) {
                triggerState = "1";
                isChangeColor = 1;
            } else if (order.getTriggerCheckState().equals((byte) 1)
                    && DealUtil.priceCompare(order.getRealPayoutFee(), new BigDecimal(0), "=")) {
                triggerState = "2";
            } else {
                triggerState = "0";
            }
            map.put("triggerState", triggerState);// 0 等待 判定 1 履行 2不履行
            map.put("isChangeColor", isChangeColor);
            map.put("maxPayoutFee", order.getMaxPayout());
            retList.add(map);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", retList);
        result.put("total", list.getTotal());
        result.put("currentPage", list.getPageNum());
        result.put("pageSize", list.getPageSize());
        return ResultVo.setResultSuccess(result);
    }

    @RequestMapping(value = "/getOrderInfo")
    public ResultVo getOrderInfo(String token, String innerOrderId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("innerOrderId", innerOrderId);
        OrderInfoVo order = orderService.selectOrderInfo(params);
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("innerOrderId", order.getInnerOrderId());
        retMap.put("buyCount", order.getBuyCount());
        retMap.put("payFee", order.getPayFee());
        retMap.put("maxPayout", order.getMaxPayout());
        retMap.put("time", DateUtil.format(order.getStime(), DateUtil.YMD_DATE_PATTERN));
        retMap.put("contractPrice", order.getContractPrice());
        retMap.put("cityName", contractService.getCityNameByCityId(order.getCityId()));
        String condition = DealUtil.getWeatherTypeStr(order.getWeatherType()) + DealUtil.getOpTypeStr(order.getOpType())
                + order.getThreshold() + DealUtil.getWeatherTypeSuffix(order.getWeatherType());
        retMap.put("condition", condition);
        String triggerState = "";
        if (order.getTriggerCheckState().equals((byte) 1)
                && DealUtil.priceCompare(order.getRealPayoutFee(), new BigDecimal(0), ">")) {
            triggerState = "1";
        } else if (order.getTriggerCheckState().equals((byte) 1)
                && DealUtil.priceCompare(order.getPayoutFee(), new BigDecimal(0), "=")) {
            triggerState = "2";
        } else {
            triggerState = "0";
        }
        retMap.put("triggerState", triggerState);//// 0 等待 判定 1 履行 2不履行
        /**
         * 触发表
         */
        OrderTrigger orderTrigger = triggerService.getTriggerByInnerOrderId(innerOrderId);
        String weatherLive = "";
        String weatherLiveUrl = "";
        if (orderTrigger != null) {
            Float weatherValue = orderTrigger.getNmcWeatherValue() == null ? orderTrigger.getCmaWeatherValue()
                    : orderTrigger.getNmcWeatherValue();
            if (weatherValue != null) {
                weatherLive = DealUtil.getWeatherTypeStr(order.getWeatherType()) + weatherValue
                        + DealUtil.getWeatherTypeSuffix(order.getWeatherType());
            }
        }
        retMap.put("weatherLive", weatherLive);
        retMap.put("weatherLiveUrl", "");
        retMap.put("qaUrl", "http://www.baotianqi.cn/qa.html");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("orderInfo", retMap);
        return ResultVo.setResultSuccess(result);
    }

    // @RequestMapping(value = "/getOrderInfo")
    // public ResultVo getOrderInfo(String token, String innerOrderId, HttpServletResponse response) {
    // response.addHeader("Access-Control-Allow-Origin", "*");
    // if (!checkLogin(token)) {
    // return ResultVo.setResultError();
    // }
    // Long customerId = Long.valueOf(getTokenId(token));
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("customerId", customerId);
    // params.put("innerOrderId", innerOrderId);
    // OrderVo orderVo = orderService.getOrderInfo(params);
    // OrderList order = orderVo.getOrderList();
    // Map<String, Object> retMap = new HashMap<String, Object>();
    // retMap.put("innerOrderId", order.getInnerOrderId());
    // retMap.put("buyTime", order.getGmtCreate());
    // retMap.put("buyCount", order.getBuyCount());
    // retMap.put("payFee", order.getPayFee());
    // retMap.put("payTime", order.getPayTime());
    // retMap.put("stime", order.getStime());
    // retMap.put("etime", order.getEtime());
    // retMap.put("opType", order.getOpType());
    // retMap.put("weatherType", order.getWeatherType());
    // retMap.put("productName", productConfigService.getProductNameByProductId(order.getProductId()));
    // List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
    // List<OrderCity> citys = orderVo.getOrderCitys();
    // for (OrderCity orderCity : citys) {
    // Map<String, Object> cityMap = new HashMap<String, Object>();
    // cityMap.put("threshold", orderCity.getThreshold());
    // cityMap.put("cityId", orderCity.getCityId());
    // cityMap.put("cityName", contractService.getCityNameByCityId(orderCity.getCityId()));
    // cityList.add(cityMap);
    // }
    // retMap.put("orderCitys", cityList);
    // Map<String, Object> result = new HashMap<String, Object>();
    //
    // result.put("orderInfo", retMap);
    // return ResultVo.setResultSuccess(result);
    // }

    private BigDecimal formtPrice(String price) {
        BigDecimal pr = new BigDecimal(price);
        return pr;
    }

    private BigDecimal priceMultiplyBuyCnt(BigDecimal price, Integer buyCnt) {
        BigDecimal cnt = new BigDecimal(buyCnt);
        return cnt.multiply(price);
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

    private boolean pay(OrderList order, OrderCity city, Customer customer) {
        try {
            orderService.insertOrder(order, city);
            customerService.updateCustomer(customer);
        } catch (Exception e) {
            logger.error("支付失败===" + e.getMessage());
            return false;
        }
        return true;
    }
}
