package com.vpp.core.system.role.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.system.role.bean.Role;
import com.vpp.core.system.role.mapper.RoleMapper;
import com.vpp.core.system.role.service.IRoleService;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public int deleteByPrimaryKey(Long id) throws Exception {
        return roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Role record) throws Exception {
        return roleMapper.insert(record);
    }

    @Override
    public Role selectByPrimaryKey(Long id) throws Exception {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Role record) throws Exception {
        return roleMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<Role> findLimitByCond(int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        return roleMapper.findLimitByCond();
    }

    @Override
    public List<Role> findAll() throws Exception {
        return roleMapper.findAll();
    }
}
