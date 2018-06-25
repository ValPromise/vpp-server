package com.vpp.core.app.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.ConstantsTemp;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.service.city.bean.CityVo;
import com.vpp.service.city.service.ICityService;

@RestController
@RequestMapping("/app/city/")
public class AppCityController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppCityController.class);

    @Autowired
    private ICityService cityService;

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

    private List<String> arrayToList(String[] array) {
        List<String> idList = new ArrayList<String>();
        for (String id : array) {
            idList.add(id);
        }
        return idList;
    }
}
