package com.vpp.core.country.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.country.bean.CountryInfo;
import com.vpp.core.country.mapper.CountryInfoMapper;
import com.vpp.core.country.service.ICountryService;

@Service
public class CountryService implements ICountryService {
	@Autowired
	private CountryInfoMapper countryInfoMapper;
	@Override
	public Page<CountryInfo> getCountryList(Integer currentPage, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(currentPage, pageSize);
		return countryInfoMapper.getCountryList(params);
	}

	
}
