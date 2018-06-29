package com.vpp.core.app.temp.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.ConstantsTemp;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.standardized.order.service.IContractService;
import com.vpp.core.weather.bean.WeatherData;
import com.vpp.core.weather.service.IWeatherService;

@RestController
@RequestMapping("/app/temp/contract")
public class AppTempContractController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppTempContractController.class);

    @Autowired
    private IContractService contractService;

    @Autowired
    private IWeatherService weatherService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 温度历史数据key
     */
    public static final String TEMP_HISTORY_KEY = "temp_history_";
    /**
     * 降雨历史数据key
     */
    public static final String RAIN_HISTORY_KEY = "rain_history_";

    @RequestMapping(value = "/getContract")
    @ResponseBody
    public ResultVo getContract(String token, String cityId, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        List<String> cityList = contractService.getHotCity();
        if (StringUtils.isBlank(cityId)) {
            cityId = cityList.get(0);// 默认查询第一个城市
        }
        if (StringUtils.isBlank(cityId) || !cityList.contains(cityId)) {
            return ResultVo.setResultError(getMessage("city_not_have"));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("city_id", cityId);
        map.put("template_id", ConstantsTemp.TEMPLATE_ID);
        map.put("date_ref", DateUtil.format(DateUtil.addDate(new Date(), 1), DateUtil.YMD_DATE_TIME_PATTERN));
        map.put("risk_control", String.valueOf(1));
        Map<String, Object> contract = contractService.getContract(map);
        if (contract == null) {
            return ResultVo.setResultError();
        }
        String explainFirst = getMessage("temp_explain_first");
        String explainSecond = MessageFormat.format(getMessage("temp_explain_second"),
                contractService.getCityInfos().get(map.get("city_id")), map.get("city_id"));
        contract.put("explainFirst", explainFirst);
        contract.put("explainSecond", explainSecond);
        String contractTitle = MessageFormat.format(getMessage("contract_title"),
                DateUtil.strToStr(map.get("date_ref").toString(), DateUtil.YMD_DATE_TIME_PATTERN, DateUtil.YMD_DATE_PATTERN),
                contractService.getCityNameByCityId(map.get("city_id").toString()));
        contract.put("contractTitle", contractTitle);
        contract.put("maxBuyCount", ConstantsTemp.MAX_BUY_COUNT);
        contract.put("history", this.tempHistory(cityId));

        return ResultVo.setResultSuccess(contract);
    }

    @RequestMapping(value = "/getDefaultContract")
    @ResponseBody
    public ResultVo getDefaultContract(String token, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        List<String> cityList = contractService.getHotCity();
        if (cityList.size() == 0) {
            return ResultVo.setResultError(getMessage("city_not_have"));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("city_id", cityList.get(0));
        map.put("template_id", ConstantsTemp.TEMPLATE_ID);
        map.put("date_ref", DateUtil.format(DateUtil.addDate(new Date(), 1), DateUtil.YMD_DATE_TIME_PATTERN));
        map.put("risk_control", String.valueOf(1));
        Map<String, Object> contract = contractService.getContract(map);
        if (null == contract) {
            return ResultVo.setResultError();
        }
        contract.put("defaultCityId", cityList.get(0));
        contract.put("defaultCityName", contractService.getCityNameByCityId(cityList.get(0)));
        contract.put("citys", contractService.getCitys());
        String explainFirst = getMessage("temp_explain_first");
        String explainSecond = MessageFormat.format(getMessage("temp_explain_second"),
                contractService.getCityInfos().get(map.get("city_id")), map.get("city_id"));
        contract.put("explainFirst", explainFirst);
        contract.put("explainSecond", explainSecond);
        String contractTitle = MessageFormat.format(getMessage("contract_title"),
                DateUtil.strToStr(map.get("date_ref").toString(), DateUtil.YMD_DATE_TIME_PATTERN, DateUtil.YMD_DATE_PATTERN),
                contractService.getCityNameByCityId(map.get("city_id").toString()));
        contract.put("contractTitle", contractTitle);
        contract.put("maxBuyCount", ConstantsTemp.MAX_BUY_COUNT);// 最大购买份数
        contract.put("history", this.tempHistory(cityList.get(0)));
        return ResultVo.setResultSuccess(contract);
    }

    /**
     * 天气行情
     * 
     * @author Lxl
     * @param cityId
     * @return
     */
    private List<Map<String, Object>> tempHistory(String cityId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String key = TEMP_HISTORY_KEY + cityId;
            // 缓存
            if (redisTemplate.hasKey(key)) {
                list = (List<Map<String, Object>>) redisTemplate.opsForValue().get(key);
                return list;
            }
            Date eDate = DateUtil.getYesterday();
            Date sDate = DateUtil.diffDate(eDate, 7);
            List<String> dates = DateUtil.getDatesBetweenTwoDate(sDate, eDate, DateUtil.YMD_DATE_TIME_PATTERN);
            for (String date : dates) {
                WeatherData weatherData = weatherService.selectWeatherTemp(cityId, date);
                Float maxTemp = weatherData.getRealMaxTemp();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("type", date.substring(date.length() - 2));
                data.put("value", maxTemp);
                list.add(data);
            }
            redisTemplate.opsForValue().set(TEMP_HISTORY_KEY + cityId, list, 1, TimeUnit.HOURS);
        } catch (Exception e) {
        }
        return list;
    }
}
