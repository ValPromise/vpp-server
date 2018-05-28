package com.vpp.core.system.resource.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.vpp.core.system.resource.bean.Resource;

/**
 * 菜单
 * 
 * @author Lxl
 * @version V1.0 2018年5月4日
 */
public interface IResourceService {
    int deleteByPrimaryKey(Long id) throws Exception;

    int insert(Resource record) throws Exception;

    Resource selectByPrimaryKey(Long id) throws Exception;

    int updateByPrimaryKey(Resource record) throws Exception;

    Page<Resource> findLimitByCond(int pageNum, int pageSize, Long pid) throws Exception;

    List<Resource> findListByUserId(Long userId) throws Exception;

    List<Resource> findAll() throws Exception;

    List<Resource> findListByRoleIds(List<Long> roleIds) throws Exception;

    List<Resource> findParentListByRoleIds(List<Long> roleIds) throws Exception;
}
