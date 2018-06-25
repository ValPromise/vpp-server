package com.vpp.core.geetest.controller;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.geetest.GeetestConfig;
import com.vpp.common.geetest.GeetestLib;
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.RequestUtils;

import net.sf.json.JSONObject;

/**
 * 极验验证
 * 
 * @author Lxl
 * @version V1.0 2018年5月24日
 */
@RestController
@RequestMapping(value = "/geetest")
public class GeetestController {
    private static final Logger logger = LogManager.getLogger(GeetestController.class);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 极验验证 行为验证 第一步
     * 
     * @author Lxl
     * @param mobile
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/geeFirst")
    public String geeFirst(String mobile, HttpServletResponse response, HttpServletRequest request) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isBlank(mobile)) {
            return "";
        }
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());
        String resStr = "";

        String ip = RequestUtils.getRemoteAddress(request);
//        logger.info("request ip : {}", ip);
        // 自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", mobile); // 网站用户id
        param.put("client_type", "native"); // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("ip_address", ip); // 传输用户请求验证时所携带的IP

        // 进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);
        String key = GeetestConfig.GEETEST_REDIS_HASH_PREFIX + mobile;
        // 将服务器状态设置到session中
        redisTemplate.opsForHash().put(key, gtSdk.gtServerStatusSessionKey, gtServerStatus);
        redisTemplate.opsForHash().put(key, GeetestConfig.GEETEST_REDIS_USERID, mobile);

        redisTemplate.expire(key, Constants.CACHE_TIME_MINUTE * 5, TimeUnit.SECONDS);// 缓存五分钟

        // redisTemplate.opsForValue().set(key, gtServerStatus, Constants.CACHE_TIME_DAY, TimeUnit.SECONDS);
        // request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        // // 将userid设置到session中
        // request.getSession().setAttribute("userid", userid);

        // 获取本次验证初始化返回字符串
        resStr = gtSdk.getResponseStr();

        return resStr;
    }

    /**
     * 极验验证 行为验证 第二步
     * 
     * @author Lxl
     * @param mobile
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/geeSecond")
    public JSONObject geeSecond(String mobile, HttpServletResponse response, HttpServletRequest request) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isBlank(mobile)) {
            JSONObject data = new JSONObject();
            return data;
        }

        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        String ip = RequestUtils.getRemoteAddress(request);
//        logger.info("request ip : {}", ip);

        // 从session中获取gt-server状态
        String key = GeetestConfig.GEETEST_REDIS_HASH_PREFIX + mobile;
        // 将服务器状态设置到session中
        int gt_server_status_code = (Integer) redisTemplate.opsForHash().get(key, gtSdk.gtServerStatusSessionKey);
        // 从session中获取userid
        String userid = (String) redisTemplate.opsForHash().get(key, GeetestConfig.GEETEST_REDIS_USERID);

        // 自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userid); // 网站用户id
        param.put("client_type", "native"); // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("ip_address", ip); // 传输用户请求验证时所携带的IP

        int gtResult = 0;
        if (gt_server_status_code == 1) {
            // gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            // System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            // System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        if (gtResult == 1) {
            // 验证成功
            JSONObject data = new JSONObject();
            data.put("status", "success");
            data.put("version", gtSdk.getVersionInfo());
            // 缓存极验验证成功结果，如果不成功禁止登陆和获取短信操作
//            logger.debug("geetest success ::: {}", key);
            redisTemplate.opsForHash().put(key, GeetestConfig.GEETEST_REDIS_SUCCESS, DateUtil.getCurrentDateTimeLocal());
            return data;
        } else {
            // 验证失败
            JSONObject data = new JSONObject();
            data.put("status", "fail");
            data.put("version", gtSdk.getVersionInfo());
            return data;
        }

    }
}
