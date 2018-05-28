package com.vpp.core.system.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.system.user.bean.User;
import com.vpp.core.system.user.bean.UserVo;
import com.vpp.core.system.user.mapper.UserMapper;
import com.vpp.core.system.user.service.IUserService;

/**
 * 系统用户
 * 
 * @author Lxl
 * @version V1.0 2018年5月4日
 */
@Service
public class UserService implements IUserService {
    // private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public int deleteByPrimaryKey(Long id) throws Exception {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(User record) throws Exception {
        return userMapper.insert(record);
    }

    @Override
    public User selectByPrimaryKey(Long id) throws Exception {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(User record) throws Exception {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<User> findLimitByCond(int pageNum, int pageSize, String loginName) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.findLimitByCond(loginName);
    }

    @Override
    public User login(String loginName, String password) throws Exception {
        return userMapper.login(loginName, password);
    }

    @Override
    public Page<UserVo> findUserVoLimitByCond(int pageNum, int pageSize, String loginName) throws Exception {
        return userMapper.findUserVoLimitByCond(loginName);
    }

}
