package com.vpp.core.country.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.country.bean.CountryInfo;
import com.vpp.core.country.service.ICountryService;

@RestController
@RequestMapping(value = "/country")
public class CountryController {
	
	@Autowired
	private ICountryService countryService;

//	@RequestMapping(value = "/getCountryList")
//	public ResultVo getCountryList(Integer currentPage, Integer pageSize, String innerOrderId, String stime, String etime,
//			HttpServletResponse response ){
//		response.addHeader("Access-Control-Allow-Origin", "*");
//		if (null == currentPage) {
//			currentPage = new Integer(1);
//		}
//		if (null == pageSize) {
//			pageSize = new Integer(15);
//		}
//		Map<String,Object> params = new HashMap<String, Object>();
//		Page<CountryInfo> list = countryService.getCountryList(currentPage, pageSize, params);
//		PageInfo<CountryInfo> pageInfo = new PageInfo<CountryInfo>(list);
//		Map<String,Object> result = new HashMap<String, Object>();
//		result.put("rows", pageInfo.getList());
//		result.put("total", list.getTotal());
//		result.put("currentPage", list.getPageNum());
//		result.put("pageSize", list.getPageSize());
//		return ResultVo.setResultSuccess(result);
//	}
}
