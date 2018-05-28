package com.vpp.core.system.role.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.vpp.core.system.role.bean.Role;

/**
 * 角色
 * 
 * @author Lxl
 * @version V1.0 2018年5月4日
 */
public interface IRoleService {
    int deleteByPrimaryKey(Long id) throws Exception;

    int insert(Role record) throws Exception;

    Role selectByPrimaryKey(Long id) throws Exception;

    int updateByPrimaryKey(Role record) throws Exception;

    Page<Role> findLimitByCond(int pageNum, int pageSize) throws Exception;

    List<Role> findAll() throws Exception;

}
