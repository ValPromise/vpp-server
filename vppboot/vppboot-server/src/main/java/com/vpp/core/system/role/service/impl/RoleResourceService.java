package com.vpp.core.system.role.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vpp.core.system.role.bean.RoleResource;
import com.vpp.core.system.role.mapper.RoleResourceMapper;
import com.vpp.core.system.role.service.IRoleResourceService;

@Service
public class RoleResourceService implements IRoleResourceService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Transactional
    @Override
    public int update(Long roleId, List<RoleResource> records) {
        roleResourceMapper.deleteByRoleId(roleId);
        roleResourceMapper.insertResourceList(records);
        return 0;
    }

}
