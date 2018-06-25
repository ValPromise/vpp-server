package com.vpp.core.management.rain.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.utils.AppUtils;
import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.vo.AppOrderInfoVo;
import com.vpp.common.vo.AppOrderListVo;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.trigger.ITriggerService;
import com.vpp.core.standardized.trigger.OrderTrigger;

/**
 * 降雨合约 后台
 * 
 * @author Lxl
 * @version V1.0 2018年5月28日
 */
@RestController
@RequestMapping("/mg/rain")
public class MgRainOrderController extends CommonController {
    private static final Logger logger = LogManager.getLogger(MgRainOrderController.class);
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ITriggerService triggerService;

    @RequestMapping(value = "/getOrderList")
    @ResponseBody
    public ResultVo getOrderList(Integer currentPage, Integer pageSize, HttpServletResponse response) {
//        response.addHeader("Access-Control-Allow-Origin", "*");
        currentPage = null == currentPage ? 1 : currentPage;
        pageSize = null == pageSize ? 15 : pageSize;

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("productId", ConstantsRain.PRODUCT_ID);

            Page<OrderInfoVo> tempOrders = orderService.findVosByCondition(currentPage, pageSize, params);

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
    @ResponseBody
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

}
