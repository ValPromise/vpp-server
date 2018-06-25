package com.vpp.service.city.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vpp.service.city.bean.CityInfo;
import com.vpp.service.city.bean.CityVo;

public interface CityInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CityInfo record);

    int insertSelective(CityInfo record);

    CityInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CityInfo record);

    int updateByPrimaryKey(CityInfo record);

    CityInfo findCityInfoByCityId(@Param("cityId") String cityId);

    CityInfo findCityInfoByCnName(@Param("cnName") String cnName);

    List<CityVo> findListByCityIds(@Param("cityIds") List<String> cityIds);

    List<CityInfo> findAll();
}