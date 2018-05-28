package com.vpp.core.system.resource.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.system.resource.bean.Resource;

@Mapper
public interface ResourceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);

    Page<Resource> findLimitByCond(@Param("pid") Long pid);

    List<Resource> findListByRoleIds(List<Long> roleIds);
    
    List<Resource> findParentListByRoleIds(List<Long> roleIds);

    List<Resource> findAll();
}