package com.vpp.core.version.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.version.bean.Version;
import com.vpp.core.version.mapper.VersionMapper;
import com.vpp.core.version.service.IVersionService;

@Service
public class VersionService implements IVersionService {
	
	@Autowired
	private VersionMapper versionMapper;
	
	@Override
	public Page<Version> selectVersionList(int pageNum, int pageSize, Map<String, Object> params) throws Exception{
		PageHelper.startPage(pageNum, pageSize);
		return versionMapper.selectVersionList(params);
	}

	@Override
	public int updateByPrimaryKeySelective(Version record) {
		return versionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int insertSelective(Version record) {
		return versionMapper.insertSelective(record);
	}

	@Override
	public Version selectByPrimaryKey(Long id) {
		return versionMapper.selectByPrimaryKey(id);
	}
}
