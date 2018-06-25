package com.vpp.core.common;

import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.StringUtils;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;

@Component
public class CommonController {

    private static final Logger logger = LogManager.getLogger(CommonController.class);

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

    //
    @Autowired
    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    /**
     * 登录缓存token
     * 
     * @param id
     * @return
     */
    public String setLoginToken(String id) {
        String dateStr = DateUtil.format(new Date(), DateUtil.LONG_DATE_TIME_PATTERN_SSS);
        String token = MD5Utils.getMD5String(dateStr + id);
        String setToken = VPP_LOGIN_KEY + token;
        try {
            loginTokenRedis.opsForValue().set(setToken, id, VPP_LOGIN_CACHE_THIRTY_MINUTE, TimeUnit.HOURS);
        } catch (Exception e) {
            logger.error("发生异常setLoginToken():" + e.getMessage());
        }
        return token;
    }

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

    /**
     * 获取缓存客户Id
     * 
     * @param token
     * @return
     */
    public String getCustomerIdByToken(String token) {
        String customerId = null;
        try {
            if (loginTokenRedis.hasKey(VPP_LOGIN_KEY + token)) {
                customerId = loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY + token);
            }
        } catch (Exception e) {
            logger.error("getCustomerIdByToken error ::: {}", e.getMessage());
        }
        return customerId;
    }

    /**
     * 获取客户缓存ID
     * 
     * @author Lxl
     * @param token
     * @return
     */
    public Long getCustomerId(String token) {
        Long customerId = null;
        try {
            if (loginTokenRedis.hasKey(VPP_LOGIN_KEY + token)) {
                customerId = Long.valueOf(loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY + token));
            }
        } catch (Exception e) {
            logger.error("getCustomerId error ::: {}", e.getMessage());
        }
        return customerId;
    }

    /**
     * 根据
     * 
     * @author Lxl
     * @param token
     * @return
     */
    public Customer findCustomerByToken(String token) {
        try {
            if (loginTokenRedis.hasKey(VPP_LOGIN_KEY + token)) {
                String customerId = loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY + token);
                return customerService.selectCustomerById(Long.valueOf(customerId));
            }
        } catch (Exception e) {
            logger.error("findCustomerByToken error ::: {}", e.getMessage());
        }
        return null;
    }

    /**
     * del login key
     * 
     * @param token
     * @return
     */
    public boolean delLoginToken(String token) {
        try {
            loginTokenRedis.delete(VPP_LOGIN_KEY + token);
        } catch (Exception e) {
            logger.error("出现异常delLoginToken():" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 国际化语言取值
     * 
     * @param msgKey
     * @return
     */
    public String getMessage(String msgKey) {
        Locale locale = LocaleContextHolder.getLocale();
        String msg = messageSource.getMessage(msgKey, null, locale);
        return msg;
    }

    /**
     * 缓存手机验证码
     * 
     * @param mobile
     * @param code
     */
    public void setMobileCodeRedis(String mobile, String code) {
        mobileCodeTokenRedis.opsForValue().set(VPP_LOGIN_MOBILE_CODE + mobile + code, code,
                VPP_LOGIN_MOBILE_CODE_CACHE_THIRTY_MINUTE, TimeUnit.SECONDS);
    }

    /**
     * 校验手机验证码
     * 
     * @param mobile
     * @param code
     * @return
     */
    public boolean checkMobileCode(String mobile, String code) {
        String getCode = VPP_LOGIN_MOBILE_CODE + mobile + code;
        getCode = mobileCodeTokenRedis.opsForValue().get(getCode);
        if (getCode == null) {
            return false;
        }
        return true;
    }

    // 生成随机数字和字母,
    public String getInviteCode(int length) {
        String val = "";
        Random random = new Random();
        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
