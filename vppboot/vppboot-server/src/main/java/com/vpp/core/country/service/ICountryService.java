package com.vpp.core.country.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.country.bean.CountryInfo;

public interface ICountryService {
	Page<CountryInfo> getCountryList(Integer currentPage, Integer pageSize, Map<String, Object> params);
}
