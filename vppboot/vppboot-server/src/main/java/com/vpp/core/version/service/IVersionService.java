package com.vpp.core.version.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.version.bean.Version;

public interface IVersionService {
    Version selectByPrimaryKey(Long id);

    int insertSelective(Version record);

    int updateByPrimaryKeySelective(Version record);

    Page<Version> selectVersionList(int pageNum, int pageSize, Map<String, Object> params) throws Exception;

    List<Version> findByVersionNo(int versionNo);
}
