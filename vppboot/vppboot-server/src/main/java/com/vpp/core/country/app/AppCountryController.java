package com.vpp.core.country.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.country.bean.CountryInfo;
import com.vpp.core.country.service.ICountryService;

@RestController
@RequestMapping("/app/country")
public class AppCountryController {
	@Autowired
	private ICountryService countryService;

	@RequestMapping(value = "/getCountryList")
	public ResultVo getCountryList(Integer currentPage, Integer pageSize, String innerOrderId, String stime, String etime,
			HttpServletResponse response ){
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (null == currentPage) {
			currentPage = new Integer(1);
		}
		if (null == pageSize) {
			pageSize = new Integer(15);
		}
		Map<String,Object> params = new HashMap<String, Object>();
		Page<CountryInfo> list = countryService.getCountryList(currentPage, pageSize, params);
		PageInfo<CountryInfo> pageInfo = new PageInfo<CountryInfo>(list);
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (CountryInfo country : pageInfo.getList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("countryUs", country.getCountryUs());
			map.put("countryCode",country.getCountryCode());
			map.put("countryName", country.getCountryName());
			map.put("countryCn", country.getCountryCn());
			retList.add(map);
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("rows", retList);
		result.put("total", list.getTotal());
		result.put("currentPage", list.getPageNum());
		result.put("pageSize", list.getPageSize());
		return ResultVo.setResultSuccess(result);
	}
}
