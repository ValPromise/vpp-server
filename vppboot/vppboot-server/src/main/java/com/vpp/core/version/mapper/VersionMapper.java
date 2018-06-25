package com.vpp.core.version.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.version.bean.Version;

@Mapper
public interface VersionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Version record);

    int insertSelective(Version record);

    Version selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);

    Page<Version> selectVersionList(Map<String, Object> params) throws Exception;

    List<Version> findByVersionNo(@Param("versionNo") int versionNo);
}