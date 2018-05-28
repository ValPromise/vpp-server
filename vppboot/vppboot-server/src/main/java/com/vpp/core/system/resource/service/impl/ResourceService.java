package com.vpp.core.system.resource.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.system.resource.bean.Resource;
import com.vpp.core.system.resource.mapper.ResourceMapper;
import com.vpp.core.system.resource.service.IResourceService;
import com.vpp.core.system.role.mapper.UserRoleMapper;

/**
 * 菜单
 * 
 * @author Lxl
 * @version V1.0 2018年5月4日
 */
@Service
public class ResourceService implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long id) throws Exception {
        return resourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Resource record) throws Exception {
        return resourceMapper.insert(record);
    }

    @Override
    public Resource selectByPrimaryKey(Long id) throws Exception {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Resource record) throws Exception {
        return resourceMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<Resource> findLimitByCond(int pageNum, int pageSize, Long pid) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        return resourceMapper.findLimitByCond(pid);
    }

    @Override
    public List<Resource> findListByUserId(Long userId) throws Exception {
        List<Long> roleIds = userRoleMapper.findRoleIdsByUserId(userId);
        List<Resource> res = resourceMapper.findListByRoleIds(roleIds);
        return res;
    }

    @Override
    public List<Resource> findAll() throws Exception {
        return resourceMapper.findAll();
    }

    @Override
    public List<Resource> findListByRoleIds(List<Long> roleIds) throws Exception {
        return resourceMapper.findListByRoleIds(roleIds);
    }

    @Override
    public List<Resource> findParentListByRoleIds(List<Long> roleIds) throws Exception {
        return resourceMapper.findParentListByRoleIds(roleIds);
    }
}
