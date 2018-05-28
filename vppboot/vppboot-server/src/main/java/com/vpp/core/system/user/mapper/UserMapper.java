package com.vpp.core.system.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.system.user.bean.User;
import com.vpp.core.system.user.bean.UserVo;

/**
 * 系统用户
 * 
 * @author Lxl
 * @version V1.0 2018年5月4日
 */
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Page<User> findLimitByCond(@Param("loginName") String loginName);

    Page<UserVo> findUserVoLimitByCond(@Param("loginName") String loginName);

    User login(@Param("loginName") String loginName, @Param("password") String password);
}