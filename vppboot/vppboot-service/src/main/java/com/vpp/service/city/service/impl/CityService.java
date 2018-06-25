package com.vpp.service.city.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpp.service.city.bean.CityInfo;
import com.vpp.service.city.bean.CityVo;
import com.vpp.service.city.mapper.CityInfoMapper;
import com.vpp.service.city.service.ICityService;

@Service
public class CityService implements ICityService {

    // private static final Logger logger = LogManager.getLogger(CityService.class);

    @Autowired
    private CityInfoMapper cityInfoMapper;

    @Override
    public CityInfo findCityInfoByCityId(String cityId) {
        return cityInfoMapper.findCityInfoByCityId(cityId);
    }

    @Override
    public CityInfo findCityInfoByCnName(String cnName) {
        return cityInfoMapper.findCityInfoByCnName(cnName);
    }

    @Override
    public List<CityVo> findListByCityIds(List<String> cityIds) {
        return cityInfoMapper.findListByCityIds(cityIds);
    }

    @Override
    public List<CityInfo> findAll() {
        return cityInfoMapper.findAll();
    }

}
