package com.vpp.common.utils;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.vpp.core.customer.service.ICustomerService;

@Component
public class AppLoginUtils {
    private static final Logger logger = LogManager.getLogger(AppLoginUtils.class);

    public static final int VPP_LOGIN_CACHE_THIRTY_MINUTE = 24;// 24小时

    public static final String VPP_LOGIN_KEY = "vpp_login_key_";

    public static final String VPP_LOGIN_MOBILE_CODE = "vpp_login_mobile_code_";

    public static final int VPP_LOGIN_MOBILE_CODE_CACHE_THIRTY_MINUTE = 60 * 5;// 5分钟

    // public static final String TOKEN_FAIL_ERROR_CODE = "1001";

    public static final String VPP_IMEI_KEY = "vpp_imei_key_";

    @Autowired
    private RedisTemplate<String, String> loginTokenRedis;
    @Autowired
    private RedisTemplate<String, String> mobileCodeTokenRedis;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ICustomerService customerService;

    /**
     * 校验登录状态
     * 
     * @param token
     * @return
     */
    public boolean checkLogin(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        String customerId = null;
        try {
            customerId = loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY + token);
        } catch (Exception e) {
            logger.error("出现异常checkLoginToken():" + e.getMessage());
            return false;
        }
        if (StringUtils.isEmpty(customerId)) {
            return false;
        }
        loginTokenRedis.opsForValue().set(token, customerId, VPP_LOGIN_CACHE_THIRTY_MINUTE, TimeUnit.HOURS);
        return true;
    }
}