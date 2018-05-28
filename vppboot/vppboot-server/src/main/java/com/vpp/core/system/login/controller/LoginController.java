package com.vpp.core.system.login.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.Constants;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.LoginUtils;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.TreeBuilder;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.notice.controller.NoticeController;
import com.vpp.core.system.resource.bean.MenuVo;
import com.vpp.core.system.resource.bean.Resource;
import com.vpp.core.system.resource.service.IResourceService;
import com.vpp.core.system.user.bean.User;
import com.vpp.core.system.user.service.IUserService;

import net.sf.json.JSONArray;

/**
 * 系统公告管理
 * 
 * @author Lxl
 * @version V1.0 2018年4月18日
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LogManager.getLogger(NoticeController.class);
    @Autowired
    LoginUtils loginUtils;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IUserService userService;
    @Autowired
    private IResourceService resourceService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public ResultVo login(String username, String password, HttpServletRequest request) {
        // String u = "admin";
        // String p = MD5Utils.getMD5String("2016xttqb");

        String pwdToken = LoginUtils.getPasswordToken(password);
        try {
            User user = userService.login(username, pwdToken);
            if (null != user) {
                String token = MD5Utils.getMD5String(DateUtil.getCurrentDateTimeLocal() + pwdToken);
                // 缓存token
                redisTemplate.opsForValue().set(ConstantsServer.Redis.TOKEN_KEY + token, 1001L, Constants.CACHE_TIME_HOUR,
                        TimeUnit.SECONDS);

                // 菜单权限
                List<Resource> resources = resourceService.findListByUserId(user.getId());
                List<MenuVo> menus = loginUtils.getMenuTree(resources);
                JSONArray jsonarray = JSONArray.fromObject(menus);

                redisTemplate.opsForValue().set(ConstantsServer.Redis.TOKEN_AUTH_KEY + token, jsonarray,
                        Constants.CACHE_TIME_HOUR, TimeUnit.SECONDS);

                Map<String, Object> result = new HashMap<String, Object>();
                result.put("token", token);
                result.put("resources", jsonarray);
                return ResultVo.setResultSuccess(result);
            } else {
                return ResultVo.setResultError("username or password error ");
            }
        } catch (Exception e) {
            logger.error("error ::: {}", e.getMessage());
            return ResultVo.setResultError();
        }
    }

    @RequestMapping(value = "/getMenu")
    @ResponseBody
    public ResultVo getMenu(String token, HttpServletRequest request) {
        try {
            JSONArray jsonarray = (JSONArray) redisTemplate.opsForValue().get(ConstantsServer.Redis.TOKEN_AUTH_KEY + token);
            return ResultVo.setResultSuccess(jsonarray);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public ResultVo logout(String token, HttpServletRequest request) {
        try {
            redisTemplate.delete(ConstantsServer.Redis.TOKEN_KEY + token);
            redisTemplate.delete(ConstantsServer.Redis.TOKEN_AUTH_KEY + token);
            return ResultVo.setResultSuccess(token);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }
    }

    private List<MenuVo> findChildren(Resource parentRes, List<Resource> resources) {
        List<MenuVo> childrenMenus = new ArrayList<MenuVo>();
        for (Resource resChild : resources) {
            if (parentRes.getId().longValue() == resChild.getPid().longValue()) {
                MenuVo menuChild = new MenuVo();
                menuChild.setId(resChild.getId());
                menuChild.setHref(resChild.getUrl());
                menuChild.setLabel(resChild.getName());
                menuChild.setIcon(resChild.getIcon());
                childrenMenus.add(menuChild);
            }
        }
        return childrenMenus;
    }

}
