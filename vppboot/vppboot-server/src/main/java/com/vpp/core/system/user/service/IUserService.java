package com.vpp.core.system.user.service;

import com.github.pagehelper.Page;
import com.vpp.core.system.user.bean.User;
import com.vpp.core.system.user.bean.UserVo;

/**
 * 系统用户
 * 
 * @author Lxl
 * @version V1.0 2018年5月4日
 */
public interface IUserService {
    int deleteByPrimaryKey(Long id) throws Exception;

    int insert(User record) throws Exception;

    User selectByPrimaryKey(Long id) throws Exception;

    int updateByPrimaryKey(User record) throws Exception;

    Page<User> findLimitByCond(int pageNum, int pageSize, String loginName) throws Exception;

    Page<UserVo> findUserVoLimitByCond(int pageNum, int pageSize, String loginName) throws Exception;

    User login(String loginName, String password) throws Exception;
}
