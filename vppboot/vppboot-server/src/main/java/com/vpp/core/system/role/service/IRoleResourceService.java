package com.vpp.core.system.role.service;

import java.util.List;

import com.vpp.core.system.role.bean.RoleResource;

public interface IRoleResourceService {

    int update(Long roleId, List<RoleResource> records);
}
