package com.vpp.core.standardized.order.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.standardized.order.service.IContractService;

@RestController
@RequestMapping(value = "/app/city")
public class AppCityController extends CommonController {
    @Autowired
    private IContractService contractService;

    @RequestMapping(value = "/getHotCity")
    public ResultVo getHotCity(String token, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token) || true) {
            return ResultVo.setResultError("temporarily closed");
        }
        Map<String, String> cityInfos = contractService.getCityInfos();
        List<String> list = contractService.getHotCity();
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> cityMap = new HashMap<String, String>();
            cityMap.put("cityId", list.get(i));
            cityMap.put("cityName", cityInfos.get(list.get(i)));
            retList.add(cityMap);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("citys", retList);
        return ResultVo.setResultSuccess(result);
    }
}
