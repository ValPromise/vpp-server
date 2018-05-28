package com.vpp.common.utils;

/**
 * 系统常量
 * 
 * @author Lxl
 * @version V1.0 2017年12月18日
 */
public interface Constants {
    /**
     * 外部接口连接前缀
     */
    // public static final String SERVER_PREFIX = "http://localhost:8081/tqbserver/";

    /**
     * 缓存时间-月
     */
    public static final long CACHE_TIME_MONTHED = 60 * 60 * 24 * 30;

    /**
     * 缓存时间-天
     */
    public static final long CACHE_TIME_DAY = 60 * 60 * 24;

    /**
     * 缓存时间-小时
     */
    public static final long CACHE_TIME_HOUR = 60 * 60;

    /**
     * 缓存时间-分钟
     */
    public static final long CACHE_TIME_MINUTE = 60;

    /**
     * 当前服务环境
     * 
     * @author Lxl
     * @version V1.0 2018年5月26日
     */
    public class SpringProfilesActive {
        // development , production , test
        /**
         * 开发
         */
        public static final String DEVELOPMENT = "development";
        /**
         * 生产
         */
        public static final String PRODUCTION = "production";

        /**
         * 测试
         */
        public static final String TEST = "test";
    }

    public class Redis {

        /**
         * 查询合约缓存订单，保单，保单城市数据
         */
        public static final String CACHE_ORDER_POLICY_CITY = "contract_order_policy_city_";

    }

    /**
     * 创建钱包默认密码， 20180523
     */
    public static final String DEFAULT_ACCOUNT_PASSWORD = "vpp2018tqb2018";

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
}
