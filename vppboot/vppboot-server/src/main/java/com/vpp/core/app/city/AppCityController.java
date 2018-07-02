package com.vpp.core.app.city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.ConstantsTemp;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.weather.bean.WeatherData;
import com.vpp.core.weather.service.IWeatherService;
import com.vpp.service.city.bean.CityVo;
import com.vpp.service.city.service.ICityService;

@RestController
@RequestMapping("/app/city/")
public class AppCityController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppCityController.class);

    @Autowired
    private ICityService cityService;
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

    // @Autowired
    // private IProductConfigService productConfigService;
    /**
     * 高温合约城市
     * 
     * @author Lxl
     * @param response
     * @return
     */
    @RequestMapping("/temp/list")
    public ResultVo tempList(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // ProductConfig productConfig = productConfigService.selectProductByProductId(ConstantsOrder.Product.PRODUCT_ID_TEMP);
        // 高温合约产品城市ID
        List<String> idList = this.arrayToList(ConstantsTemp.CITY_IDS.split(","));
        List<CityVo> citys = cityService.findListByCityIds(idList);

        // 热门城市ID
        List<String> hotList = this.arrayToList(ConstantsTemp.HOT_CITY_IDS.split(","));
        List<CityVo> hotCitys = cityService.findListByCityIds(hotList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hot", hotCitys);
        result.put("citys", citys);
        return ResultVo.setResultSuccess(result);
    }

    /**
     * 降雨合约城市
     * 
     * @author Lxl
     * @param response
     * @return
     */
    @RequestMapping("/rain/list")
    public ResultVo rainList(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // ProductConfig productConfig = productConfigService.selectProductByProductId(ConstantsOrder.Product.PRODUCT_ID_TEMP);
        // 高温合约产品城市ID
        List<String> idList = this.arrayToList(ConstantsRain.CITY_IDS.split(","));
        List<CityVo> citys = cityService.findListByCityIds(idList);

        // 热门城市ID
        List<String> hotList = this.arrayToList(ConstantsRain.HOT_CITY_IDS.split(","));
        List<CityVo> hotCitys = cityService.findListByCityIds(hotList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hot", hotCitys);
        result.put("citys", citys);
        return ResultVo.setResultSuccess(result);
    }

    /**
     * 根据城市查询前一周的温度 天气数据
     * 
     * @author Lxl
     * @param cityId
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/temp/history")
    public ResultVo tempHistory(String cityId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        try {
            String key = TEMP_HISTORY_KEY + cityId;
            Map<String, String> data = new HashMap<String, String>();
            // 缓存
            if (redisTemplate.hasKey(key)) {
                data = (Map<String, String>) redisTemplate.opsForValue().get(key);
                return ResultVo.setResultSuccess(data);
            }
            Date eDate = DateUtil.getYesterday();
            Date sDate = DateUtil.diffDate(eDate, 7);
            List<String> dates = DateUtil.getDatesBetweenTwoDate(sDate, eDate, DateUtil.YMD_DATE_TIME_PATTERN);
            for (String date : dates) {
                WeatherData weatherData = weatherService.selectWeatherTemp(cityId, date);
                Float maxTemp = weatherData.getRealMaxTemp();
                data.put(date, maxTemp.toString());
            }
            redisTemplate.opsForValue().set(TEMP_HISTORY_KEY + cityId, data, 1, TimeUnit.DAYS);
            return ResultVo.setResultSuccess(data);
        } catch (Exception e) {
        }
        return ResultVo.setResultError();
    }

    /**
     * 根据城市查询前一周的降雨 天气数据
     * 
     * @author Lxl
     * @param cityId
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/rain/history")
    public ResultVo rainHistory(String cityId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        try {
            String key = RAIN_HISTORY_KEY + cityId;
            Map<String, String> data = new HashMap<String, String>();
            // 缓存
            if (redisTemplate.hasKey(key)) {
                data = (Map<String, String>) redisTemplate.opsForValue().get(key);
                return ResultVo.setResultSuccess(data);
            }
            Date eDate = DateUtil.getYesterday();
            Date sDate = DateUtil.diffDate(eDate, 7);
            List<String> dates = DateUtil.getDatesBetweenTwoDate(sDate, eDate, DateUtil.YMD_DATE_TIME_PATTERN);
            for (String date : dates) {
                WeatherData weatherData = weatherService.selectWeatherTemp(cityId, date);
                Float maxTemp = weatherData.getRealPrcp();
                data.put(date, maxTemp.toString());
            }
            redisTemplate.opsForValue().set(RAIN_HISTORY_KEY + cityId, data, 1, TimeUnit.DAYS);
            return ResultVo.setResultSuccess(data);
        } catch (Exception e) {
        }
        return ResultVo.setResultError();
    }

    private List<String> arrayToList(String[] array) {
        List<String> idList = new ArrayList<String>();
        for (String id : array) {
            idList.add(id);
        }
        return idList;
    }
}
