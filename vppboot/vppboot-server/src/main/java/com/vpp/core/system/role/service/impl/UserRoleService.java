package com.vpp.core.system.role.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vpp.core.system.role.bean.UserRole;
import com.vpp.core.system.role.mapper.UserRoleMapper;
import com.vpp.core.system.role.service.IUserRoleService;

@Service
public class UserRoleService implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userRoleMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int insert(UserRole record) {
        userRoleMapper.deleteByUserId(record.getUserId());
        return userRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(UserRole record) {
        return userRoleMapper.insertSelective(record);
    }

    @Override
    public UserRole selectByPrimaryKey(Long id) {
        return userRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserRole record) {
        return userRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserRole record) {
        return userRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        return userRoleMapper.findRoleIdsByUserId(userId);
    }

    @Override
    public int deleteByUserId(Long userId) {
        return userRoleMapper.deleteByUserId(userId);
    }

}
