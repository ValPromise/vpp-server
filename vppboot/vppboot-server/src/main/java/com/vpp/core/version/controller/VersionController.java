package com.vpp.core.version.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.version.bean.Version;
import com.vpp.core.version.bean.VersionVo;
import com.vpp.core.version.service.IVersionService;

@RestController
@RequestMapping(value = "/version")
public class VersionController {
	private static final Logger logger = LogManager.getLogger(VersionController.class);

	@Autowired
	private IVersionService versionService;
	/**
	 * 获取版本管理列表
	 * @author vipcgp
	 * @param page
	 * @param rows
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ResultVo list(Integer currentPage, Integer pageSize, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			if(currentPage == null){
				currentPage = new Integer(1);
			}
			if(pageSize == null){
				pageSize = new Integer(15);
			}
			Map<String,Object> params = new HashMap<String, Object>();
			Page<Version> list = versionService.selectVersionList(currentPage, pageSize, params);
			PageInfo<Version> pageInfos = new PageInfo<Version>(list);
			result.put("result", pageInfos.getList());
			result.put("total", list.getTotal());
	        result.put("currentPage", list.getPageNum());
	        result.put("pageSize", list.getPageSize());
		} catch (Exception e) {
			logger.info("list error:::{}", e.getMessage());
		}
		return ResultVo.setResultSuccess(result);
	}
	@RequestMapping(value = "/update")
	public ResultVo update(VersionVo versionVo, HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		if(versionVo == null || versionVo.getId() == null){
			return ResultVo.setResultError("更新失败");
		}
		
		Version version = versionService.selectByPrimaryKey(versionVo.getId());
		if(version == null){
			return ResultVo.setResultError("更新失败");
		}
		version.setDescription(versionVo.getDescription());
		version.setIsPush(versionVo.getIsPush());
		version.setUrl(versionVo.getUrl());
		version.setPushTime(DateUtil.parseDate(versionVo.getPushTime()));
		version.setVersionCode(versionVo.getVersionCode());
		version.setVersionName(versionVo.getVersionName());
		version.setGmtModified(new Date());
		//需要获取登录人信息
		versionVo.setMender(0L);
		int ret = versionService.updateByPrimaryKeySelective(version);
		if(ret == 0){
			return ResultVo.setResultError("更新失败");
		}
		return ResultVo.setResultSuccess("更新成功");
	}
	
	@RequestMapping(value  = "/add")
	public ResultVo add(VersionVo versionVo, HttpServletResponse response){
		response.addHeader("Access-Control-Origin", "*");
		if(StringUtils.isBlank(versionVo.getVersionCode())){
			return ResultVo.setResultError("必填项不能为空");
		}
		Version version = new Version();
		version.setDescription(versionVo.getDescription());
		version.setIsPush(versionVo.getIsPush());
		version.setUrl(versionVo.getUrl());
		version.setPushTime(DateUtil.parseDate(versionVo.getPushTime()));
		version.setVersionCode(versionVo.getVersionCode());
		version.setVersionName(versionVo.getVersionName());
		version.setGmtCreate(new Date());
		version.setCreater(0L);
		int ret = versionService.insertSelective(version);
		if(ret == 0){
			return ResultVo.setResultError("添加失败");
		}
		return ResultVo.setResultSuccess("添加成功");
	}
}
