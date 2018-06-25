package com.vpp.core.weather.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.StringUtils;
import com.vpp.core.weather.bean.WeatherData;
import com.vpp.core.weather.service.IWeatherService;

import net.sf.json.JSONObject;

/**
 * 数据组接口，查询天气数据
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
@Service
public class WeatherService implements IWeatherService {
    private static final Logger logger = LogManager.getLogger(WeatherService.class);

    // 数据组最新查询天气api地址
    public static final String WEATHER_URL = "http://119.28.70.201:801/selfrain/getNmcCma";

    /**
     * 查询数据接口 天气温度
     * 
     * @author Lxl
     * @param cityId
     * @param date
     * @return
     */
    @Override
    public WeatherData selectWeatherTemp(String cityId, String date) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("city_id", cityId);
        params.put("date", date);
        params.put("token", MD5Utils.getMD5String(cityId + date + "tqb").toLowerCase());

        String weatherStr = HttpUtils.post(WEATHER_URL, params);
        if (null == weatherStr) {
            logger.error("获取天气数据接口失败  ::: {},{}", WEATHER_URL, params);
            return null;
        }
        JSONObject jsonObj = JSONObject.fromObject(weatherStr);
        WeatherData weather = new WeatherData();
        // 最高温度
        Float nmcVal = this.formatObject(jsonObj.get("nmc_max_temp"));
        Float cmaVal = this.formatObject(jsonObj.get("cma_max_temp"));

        // 累计降雨量
        Float nmcPrcp = this.formatObject(jsonObj.get("nmc_prcp"));
        Float cmaPrcp = this.formatObject(jsonObj.get("cma_prcp"));

        weather.setNmcMaxTemp(nmcVal);
        weather.setCmaMaxTemp(cmaVal);
        weather.setNmcPrcp(nmcPrcp);
        weather.setCmaPrcp(cmaPrcp);

        return weather;
    }

    private Float formatObject(Object obj) {
        try {
            String source = String.valueOf(obj);
            if (StringUtils.isNotBlank(source)) {
                Float sourceFloat = Float.valueOf(source);
                return sourceFloat;
            }
        } catch (Exception e) {
            logger.error("selectWeatherTemp result error :: {}", obj);
            return null;
        }
        return null;
    }
}
