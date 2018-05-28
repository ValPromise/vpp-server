package com.vpp.common.geetest;

/**
 * GeetestWeb配置文件
 */
public class GeetestConfig {

    // 填入自己的captcha_id和private_key
    private static final String geetest_id = "637513eab9958757ae3c294196230391";
    private static final String geetest_key = "235c1786353495d5ea280f6bfe15cbbf";

    /**
     * redis缓存 key前缀
     */
    public static final String GEETEST_REDIS_HASH_PREFIX = "geetest_";
    /**
     * redis缓存 二级key
     */
    public static final String GEETEST_REDIS_USERID = "userid";
    /**
     * 极验验证 成功key前缀
     */
    public static final String GEETEST_REDIS_SUCCESS = "success";

    private static final boolean newfailback = true;

    public static final String getGeetest_id() {
        return geetest_id;
    }

    public static final String getGeetest_key() {
        return geetest_key;
    }

    public static final boolean isnewfailback() {
        return newfailback;
    }

}
