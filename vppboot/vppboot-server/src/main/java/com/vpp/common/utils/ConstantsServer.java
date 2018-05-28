package com.vpp.common.utils;

/**
 * server内部常量
 * 
 * @author Lxl
 * @version V1.0 2018年4月18日
 */
public interface ConstantsServer {

    /**
     * 系统公告状态-启用
     */
    public static final Byte NOTICE_STATE_ENABLE = 1;

    /**
     * 系统公告状态-禁用
     */
    public static final Byte NOTICE_STATE_DISENABLE = 0;

    /**
     * 页面cookie中token名称
     */
    public static final String TOKEN_COOKIE_NAME = "x-access-token";

    /**
     * http请求类型，跨域请求第一步通过OPTIONS方式
     */
    public static final String OPTIONS = "OPTIONS";

    /**
     * 角色状态-启用
     */
    public static final Byte ROLE_STATE_ENABLE = 1;
    /**
     * 角色状态-禁用
     */
    public static final Byte ROLE_STATE_DISENABLE = 0;

    /**
     * 状态-启用
     */
    public static final Byte STATE_ENABLE = 1;
    /**
     * 状态-禁用
     */
    public static final Byte STATE_DISENABLE = 0;

    /**
     * 天气实况帮助链接
     */
    public static final String WEATHER_HELP_URL = "http://www.baotianqi.cn/qa.html";

    class Redis {
        public static final String TOKEN_KEY = "token_key_";

        /**
         * 用户权限KEY
         */
        public static final String TOKEN_AUTH_KEY = "token_auth_key";
    }

    class ErrorCode {
        /**
         * 认证失败
         */
        public static final String AUTH_FAIL = "4001";
    }
    
}
