package com.vpp.core.banner.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.banner.bean.Banner;

public interface IBannerService {
	
	Page<Banner> getBannerList(Integer currentPage, Integer pageSize,Map<String,Object> params);
    
	int insertSelective(Banner record);

    Banner selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Banner record);
}
