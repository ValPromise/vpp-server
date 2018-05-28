package com.vpp.core.banner.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.banner.bean.Banner;
import com.vpp.core.banner.service.IBannerService;

@RestController
@RequestMapping("/banner")
public class BannerController {

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
		Page<Banner> list = bannerService.getBannerList(currentPage, pageSize, params);
		PageInfo<Banner> pageInfos = new PageInfo<Banner>(list);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", pageInfos.getList());
		result.put("total", list.getTotal());
		result.put("currentPage", list.getPageNum());
		result.put("pageSize", list.getPageSize());
		return ResultVo.setResultSuccess(result);
	}
	
	@RequestMapping(value = "/add")
	public ResultVo add(Banner banner,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(StringUtils.isBlank(banner.getImgurl())){
			return ResultVo.setResultError("请选择要添加的banner图片");
		}
		banner.setGmtCreate(new Date());
		int ret = bannerService.insertSelective(banner);
		if(ret == 0){
			return ResultVo.setResultError("添加失败,请稍候再试");
		}
		return ResultVo.setResultSuccess("添加成功 ");
	}
	
	
	@RequestMapping(value = "/update")
	public ResultVo update(Banner bannerVo,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(bannerVo.getId() == null){
			return ResultVo.setResultError("请选择要编辑的banner");
		}
		if(StringUtils.isBlank(bannerVo.getImgurl())){
			return ResultVo.setResultError("请选择要添加的banner图片");
		}
		Banner banner = bannerService.selectByPrimaryKey(bannerVo.getId());
		if(banner == null){
			return ResultVo.setResultError("要更新的banner不存在");
		}
		banner.setGmtModified(new Date());
		banner.setDescription(bannerVo.getDescription());
		banner.setImgurl(bannerVo.getImgurl());
		banner.setLinkurl(bannerVo.getLinkurl());
		banner.setState(bannerVo.getState());
		/**
		 * 设置更新人
		 */
		int ret = bannerService.updateByPrimaryKeySelective(banner);
		if(ret == 0){
			return ResultVo.setResultError("更新失败,请稍候再试");
		}
		return ResultVo.setResultSuccess("更新成功 ");
	}
}
