package com.vpp.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.vpp.core.system.resource.bean.MenuVo;
import com.vpp.core.system.resource.bean.Resource;

import net.sf.json.JSONArray;

@Component
public class LoginUtils {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_NAME = "X-Token";

    /**
     * 获取前端token对应登录用户ID
     * 
     * @author Lxl
     * @param request
     * @return
     */
    public Long getLoginAccount(HttpServletRequest request) {
        Long account = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                String tokenCookieName = cookie.getName();
                if (ConstantsServer.TOKEN_COOKIE_NAME.equals(tokenCookieName)) {
                    String token = cookie.getValue();
                    if (StringUtils.isNotBlank(token)) {
                        account = (Long) redisTemplate.opsForValue().get(ConstantsServer.Redis.TOKEN_KEY + token);
                        if (null != account) {
                            // 重置权限缓存
                            this.resetRedisAccount(token, account);
                            return account;
                        }
                    }
                }
            }
        }

        String hToken = request.getHeader(TOKEN_NAME);
        if (StringUtils.isNotBlank(hToken)) {
            account = (Long) redisTemplate.opsForValue().get(ConstantsServer.Redis.TOKEN_KEY + hToken);
            if (null != account) {
                // 重置权限缓存
                this.resetRedisAccount(hToken, account);
                return account;
            }
        }

        return account;
    }

    /**
     * 加密password
     * 
     * @author Lxl
     * @param username
     * @param password
     * @return
     */
    public static String getPasswordToken(String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);
        String pwdToken = SecurityUtils.getSign(params);
        return pwdToken;
    }

    private void resetRedisAccount(String token, Long account) {
        JSONArray jsonarray = (JSONArray) redisTemplate.opsForValue().get(ConstantsServer.Redis.TOKEN_AUTH_KEY + token);
        redisTemplate.opsForValue().set(ConstantsServer.Redis.TOKEN_AUTH_KEY + token, jsonarray, Constants.CACHE_TIME_MONTHED,
                TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(ConstantsServer.Redis.TOKEN_KEY + token, account, Constants.CACHE_TIME_HOUR,
                TimeUnit.SECONDS);
    }

    /*
     * { id: 21, label: "系统公告管理", href: '/admin/notice', icon: 'icon-caidanguanli', meta: {}, children: [] }
     */
    /**
     * 获取鉴权树形控件，需要空mate和children
     * 
     * @author Lxl
     * @param resources
     * @return
     */
    public List<MenuVo> getMenuTree(List<Resource> resources) {
        List<MenuVo> menus = new ArrayList<MenuVo>();

        Iterator<Resource> it = resources.iterator();
        while (it.hasNext()) {
            Resource res = it.next();
            MenuVo menu = new MenuVo();
            menu.setId(res.getId());
            menu.setHref(res.getUrl());
            menu.setLabel(res.getName());
            menu.setIcon(res.getIcon());
            menu.setParentId(res.getPid());
            menus.add(menu);
        }

        TreeBuilder tb = new TreeBuilder();
        return tb.buildListToTree(menus);
    }

    /**
     * 获取菜单管理树形控件
     * 
     * @author Lxl
     * @param resources
     * @return
     */
    public List<MenuVo> getMenuTreeData(List<Resource> resources) {
        List<MenuVo> menus = new ArrayList<MenuVo>();

        Iterator<Resource> it = resources.iterator();
        while (it.hasNext()) {
            Resource res = it.next();
            MenuVo menu = new MenuVo();
            menu.setId(res.getId());
            menu.setHref(res.getUrl());
            menu.setLabel(res.getName());
            menu.setIcon(res.getIcon());
            menu.setParentId(res.getPid());
            menu.setMeta("");
            menu.setChildren(null);
            menus.add(menu);
        }

        TreeBuilder tb = new TreeBuilder();
        return tb.buildListToTree(menus);
    }
}