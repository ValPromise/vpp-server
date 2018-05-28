package com.vpp.core.standardized.order.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.standardized.order.service.IContractService;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.trigger.ITriggerService;
import com.vpp.core.standardized.trigger.OrderTrigger;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

	private static final Logger logger = LogManager.getLogger(OrderController.class);
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IContractService contractService;
	@Autowired
	private ITriggerService triggerService;

	@RequestMapping(value = "/getOrderList")
	public ResultVo getOrderList(Integer currentPage, Integer pageSize, String innerOrderId, String stime, String etime,
			HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
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

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stime", stime);// 开始时间
		params.put("etime", etime);// 结束时间
		Page<OrderInfoVo> list = orderService.selectOrderList(currentPage, pageSize, params);
		PageInfo<OrderInfoVo> pageInfos = new PageInfo<OrderInfoVo>(list);
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (OrderInfoVo order : pageInfos.getList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("innerOrderId", order.getInnerOrderId());
			String st;
            try {
                st = DateUtil.strToStr(order.getStime(), DateUtil.LONG_DATE_TIME_PATTERN, "MMdd");
                map.put("productName",
                        contractService.getCityNameByCityId(order.getCityId()) + st);
            } catch (Exception e) {
            }
			map.put("payoutFee", order.getPayoutFee() == null ? 0 : order.getPayoutFee());
			String condition = DealUtil.getWeatherTypeStr(order.getWeatherType())
					+ DealUtil.getOpTypeStr(order.getOpType()) + order.getThreshold()
					+ DealUtil.getWeatherTypeSuffix(order.getWeatherType());
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
	public ResultVo getOrderInfo(String innerOrderId, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("innerOrderId", innerOrderId);
		OrderInfoVo order = orderService.selectOrderInfo(params);
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("innerOrderId", order.getInnerOrderId());
		retMap.put("buyCount", order.getBuyCount());
		retMap.put("payFee", order.getPayFee());
		retMap.put("maxPayout", order.getMaxPayout());
		retMap.put("time", order.getStime());
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
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderInfo", retMap);
		return ResultVo.setResultSuccess(result);
	}
}
