package com.vpp.core.weather.service;

import com.vpp.core.weather.bean.WeatherData;

/**
 * 数据组接口，查询天气数据
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
public interface IWeatherService {

    /**
     * 数据组接口：：：根据城市，日期，查询天气数据
     * 
     * @author Lxl
     * @param cityId
     * @param date
     * @return
     * @throws Exception
     */
    WeatherData selectWeatherTemp(String cityId, String date) throws Exception;

}
