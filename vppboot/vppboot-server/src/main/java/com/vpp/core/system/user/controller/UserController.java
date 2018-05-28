package com.vpp.core.system.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.LoginUtils;
import com.vpp.common.utils.SecurityUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.system.role.bean.Role;
import com.vpp.core.system.role.bean.UserRole;
import com.vpp.core.system.role.service.IRoleService;
import com.vpp.core.system.role.service.IUserRoleService;
import com.vpp.core.system.user.bean.User;
import com.vpp.core.system.user.bean.UserVo;
import com.vpp.core.system.user.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;
    

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    LoginUtils loginUtils;

    private static final String INIT_PASSWORD = "a123456789";

    /**
     * 分页查询
     * 
     * @author Lxl
     * @param page
     * @param rows
     * @param title
     * @param response
     * @return
     */
    @RequestMapping(value = "/limit")
    @ResponseBody
    public Map<String, Object> limit(Integer page, Integer rows, String loginName, HttpServletResponse response) {
        page = page == null ? 1 : page;
        rows = rows == null ? 20 : rows;
        long total = 0;

        Page<UserVo> list = null;
        List<Role> roles = null;
        try {
            list = userService.findUserVoLimitByCond(page, rows, loginName);
            roles = roleService.findAll();
        } catch (Exception e) {
            logger.error("listNotice error ::: {}", e.getMessage());
        }
        PageInfo<UserVo> pageInfo = new PageInfo<>(list);
        if (total == 0) {// 取一次总页数
            total = pageInfo.getTotal();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("page", page);
        result.put("rows", pageInfo.getList());
        result.put("roles", roles);
        return result;
    }

    /**
     * 新增
     * 
     * @author Lxl
     * @param noticeTitle
     * @param noticeContent
     * @return
     */
    @RequestMapping(value = "/insert")
    @ResponseBody
    public ResultVo insert(String name, String loginName, String password, String phone, Byte sex, String description,
            HttpServletRequest request) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);
        String pwdToken = SecurityUtils.getSign(params);

        User user = new User();
        user.setName(loginName);
        user.setLoginName(loginName);
        user.setPassword(pwdToken);
        user.setPhone(phone);
        user.setCreateTime(DateUtil.getCurrentDateTimeLocal());
        user.setStatus(ConstantsServer.STATE_ENABLE);
        user.setSex(sex);
        user.setDescription(description);
        try {
            userService.insert(user);
        } catch (Exception e) {
            logger.error("insert error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }

    /**
     * 修改
     * 
     * @author Lxl
     * @param id
     * @param noticeTitle
     * @param noticeContent
     * @param noticeState
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultVo update(Long id, String description, String loginName, Byte sex, Byte status, HttpServletRequest request) {
        try {
            User user = userService.selectByPrimaryKey(id);
            // // 生成密码加密token
            // Map<String, String> params = new HashMap<String, String>();
            // params.put("loginName", user.getLoginName());
            // params.put("password", password);
            // String pwdToken = SecurityUtils.getSign(params);
            user.setLoginName(loginName);
            user.setDescription(description);
            user.setSex(sex);
            user.setStatus(status);
            user.setGmtModified(DateUtil.getCurrentDateTimeLocal());
            userService.updateByPrimaryKey(user);
        } catch (Exception e) {
            logger.error("update error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }

    @RequestMapping(value = "/resetPassword")
    @ResponseBody
    public ResultVo resetPassword(Long id, String editPassword, HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("password", editPassword);
            String pwdToken = SecurityUtils.getSign(params);

            User user = userService.selectByPrimaryKey(id);
            user.setPassword(pwdToken);
            user.setGmtModified(DateUtil.getCurrentDateTimeLocal());
            userService.updateByPrimaryKey(user);
        } catch (Exception e) {
            logger.error("resetPassword error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }
    
    @RequestMapping(value = "/updateRole")
    @ResponseBody
    public ResultVo updateRole(Long userId,Long roleId, HttpServletRequest request) {
        try {
            UserRole bean = new UserRole();
            bean.setUserId(userId);
            bean.setRoleId(roleId);
            userRoleService.insert(bean);
        } catch (Exception e) {
            logger.error("updateRole error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }

    /**
     * 删除
     * 
     * @author Lxl
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultVo delete(Long id) {
        try {
            userService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("delete error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }
}
