package com.vpp.core.country.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.vpp.core.country.bean.CountryInfo;

@Mapper
public interface CountryInfoMapper {
    int deleteByPrimaryKey(Long countryId);

    int insert(CountryInfo record);

    int insertSelective(CountryInfo record);

    CountryInfo selectByPrimaryKey(Long countryId);

    int updateByPrimaryKeySelective(CountryInfo record);

    int updateByPrimaryKey(CountryInfo record);
    
    Page<CountryInfo> getCountryList(Map<String, Object> params);
}