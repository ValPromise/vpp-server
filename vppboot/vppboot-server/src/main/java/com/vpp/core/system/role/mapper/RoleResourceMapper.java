package com.vpp.core.system.role.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.vpp.core.system.role.bean.RoleResource;

@Mapper
public interface RoleResourceMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByRoleId(@Param("roleId") Long roleId);

    int insert(RoleResource record);

    int insertSelective(RoleResource record);

    RoleResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleResource record);

    int updateByPrimaryKey(RoleResource record);

    /**
     * 批量新增
     * 
     * @author Lxl
     * @param record
     * @return
     */
    int insertResourceList(List<RoleResource> record);
}