package com.vpp.core.banner.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.banner.bean.Banner;
import com.vpp.core.banner.mapper.BannerMapper;

@Service
public class BannerService implements IBannerService{
	@Autowired
	private BannerMapper bannerMapper;

	@Override
	public Page<Banner> getBannerList(Integer currentPage, Integer pageSize, Map<String, Object> params) {
		PageHelper.startPage(currentPage, pageSize);
		return bannerMapper.getBannerList(params);
	}

	@Override
	public int insertSelective(Banner record) {
		return bannerMapper.insertSelective(record);
	}

	@Override
	public Banner selectByPrimaryKey(Long id) {
		return bannerMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Banner record) {
		return bannerMapper.updateByPrimaryKey(record);
	}
}
