package com.vpp.core.standardized.payout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.standardized.order.bean.OrderList;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.pay.IPayService;
import com.vpp.core.standardized.product.IProductConfigService;

@RestController
@RequestMapping(value = "/pay")
public class PayController extends CommonController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IPayService payService;
    @Autowired
    private IOrderPayoutService orderPayoutService;
    @Autowired
    private IProductConfigService productConfigService;

    @RequestMapping(value = "/payment")
    public synchronized ResultVo pay(String token, String payPassword, String innerOrderId, BigDecimal payFee,
            HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError("登录超时");
        }
        Long customerId = Long.valueOf(getTokenId(token));
        Customer customer = customerService.selectCustomerById(customerId);
        // if (StringUtils.isBlank(payPassword) || !customer.getPayPassword().equals(MD5Utils.getMD5String(payPassword.trim())))
        // {
        // return ResultVo.setResultError("支付密码错误!");
        // }
        OrderList order = orderService.getOrderByOrderId(innerOrderId);
        if (order == null) {
            return ResultVo.setResultError("需要支付的订单不存在");
        }
        if (Byte.valueOf("1").equals(order.getPayState())) {
            return ResultVo.setResultError("该订单已支付");
        }
        if (!DealUtil.priceCompare(payFee, order.getOrderPrice(), "=")) {
            return ResultVo.setResultError("金额不匹配");
        }
        // 余额小于订单金额
        if (DealUtil.priceCompare(customer.getBalance(), order.getOrderPrice(), "<")) {
            return ResultVo.setResultError("余额不足");
        }
        boolean ret = false;
        customer.setBalance(DealUtil.priceSubtract(customer.getBalance(), order.getOrderPrice()));
        order.setPayState((byte) 1);
        order.setPayTime(new Date());
        order.setPayFee(order.getOrderPrice());
        ret = payService.payCallBack(customer, order);
        if (ret) {
            return ResultVo.setResultSuccess("支付成功");
        } else {
            return ResultVo.setResultError("支付失败");
        }
    }

    @RequestMapping(value = "/paymentHistory")
    public ResultVo paymentHistory(String token, Integer currentPage, Integer pageSize, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError("登录超时");
        }
        Long customerId = Long.valueOf(getTokenId(token));
        if (customerId == null) {
            return ResultVo.setResultError();
        }
        if (null == currentPage) {
            currentPage = new Integer(1);
        }
        if (null == pageSize) {
            pageSize = new Integer(15);
        }
        Page<OrderList> list = orderService.getPaySuccessOrderByCustomerId(currentPage, pageSize, customerId);
        PageInfo<OrderList> pageInfo = new PageInfo<>(list);
        List<Map<String, Object>> retMap = new ArrayList<Map<String, Object>>();
        for (OrderList orderList : pageInfo.getList()) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("innerOrderId", orderList.getInnerOrderId());
            result.put("payTime", orderList.getPayTime());
            result.put("payFee", orderList.getPayFee());
            result.put("productId", orderList.getProductId());
            result.put("productName", productConfigService.getProductNameByProductId(orderList.getProductId()));
            retMap.add(result);
        }
        Map<String, Object> retResult = new HashMap<String, Object>();
        retResult.put("rows", retMap);
        retResult.put("total", list.getTotal());
        retResult.put("pageSize", list.getPageSize());
        retResult.put("currentPage", list.getPageNum());
        return ResultVo.setResultSuccess(retResult);
    }

    @RequestMapping(value = "/payoutHistory")
    public ResultVo payoutHistory(String token, Integer currentPage, Integer pageSize, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError("登录超时");
        }
        Long customerId = Long.valueOf(getTokenId(token));
        if (customerId == null) {
            return ResultVo.setResultError();
        }
        if (null == currentPage) {
            currentPage = new Integer(1);
        }
        if (null == pageSize) {
            pageSize = new Integer(15);
        }
        Page<OrderPayout> list = orderPayoutService.getPayOutHistoryCustomerId(currentPage, pageSize, customerId);
        PageInfo<OrderPayout> pageInfo = new PageInfo<>(list);
        List<Map<String, Object>> retMap = new ArrayList<Map<String, Object>>();
        for (OrderPayout payout : pageInfo.getList()) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("innerOrderId", payout.getInnerOrderId());
            result.put("payoutTime", payout.getOperationTime());
            result.put("payoutFee", payout.getRealPayoutFee());
            retMap.add(result);
        }
        Map<String, Object> retResult = new HashMap<String, Object>();
        retResult.put("rows", retMap);
        retResult.put("total", list.getTotal());
        retResult.put("pageSize", list.getPageSize());
        retResult.put("currentPage", list.getPageNum());
        return ResultVo.setResultSuccess(retResult);
    }
}
