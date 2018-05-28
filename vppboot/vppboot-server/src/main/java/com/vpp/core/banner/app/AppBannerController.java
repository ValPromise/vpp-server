package com.vpp.core.banner.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.banner.bean.Banner;
import com.vpp.core.banner.service.IBannerService;

@RestController
@RequestMapping("/app/banner")
public class AppBannerController {
	@Autowired
	private IBannerService bannerService;
	
	@RequestMapping(value = "/getBannerList")
	public ResultVo getBannerList(Integer currentPage, Integer pageSize, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (null == currentPage) {
			currentPage = new Integer(1);
		}
		if (null == pageSize) {
			pageSize = new Integer(15);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", 1);
		Page<Banner> list = bannerService.getBannerList(currentPage, pageSize, params);
		PageInfo<Banner> pageInfos = new PageInfo<Banner>(list);
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (Banner banner : pageInfos.getList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("linkUrl", banner.getLinkurl());
			map.put("imgUrl",banner.getImgurl());
			retList.add(map);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", retList);
		result.put("total", list.getTotal());
		result.put("currentPage", list.getPageNum());
		result.put("pageSize", list.getPageSize());
		return ResultVo.setResultSuccess(result);
	}
}
