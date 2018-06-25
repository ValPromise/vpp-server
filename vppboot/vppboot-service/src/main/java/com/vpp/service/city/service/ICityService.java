package com.vpp.service.city.service;

import java.util.List;

import com.vpp.service.city.bean.CityInfo;
import com.vpp.service.city.bean.CityVo;

/**
 * 城市
 * 
 * @author Lxl
 * @version V1.0 2017年6月24日
 */
public interface ICityService {
    /**
     * 根据城市ID查询城市信息，同时兼容5位和9位ID
     * 
     * @author Lxl
     * @param cityId
     * @return
     */
    CityInfo findCityInfoByCityId(String cityId);

    CityInfo findCityInfoByCnName(String cnName);

    List<CityVo> findListByCityIds(List<String> cityIds);

    List<CityInfo> findAll();
}
