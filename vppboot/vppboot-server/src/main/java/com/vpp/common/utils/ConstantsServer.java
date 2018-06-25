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

    /**
     * 提现状态 1：待处理,2：已执行打款,3：处理成功,4：拒绝，5：处理失败
     */
    public static final Byte WITHDRAWAL_STATE_WAIT = 1;

    /**
     * 提现状态 2：已执行打款
     */
    public static final Byte WITHDRAWAL_STATE_TRANSFER = 2;

    /**
     * 提现状态 3：处理成功
     */
    public static final Byte WITHDRAWAL_STATE_SUCCESS = 3;

    /**
     * 提现状态 4：拒绝
     */
    public static final Byte WITHDRAWAL_STATE_REJECT = 4;

    /**
     * 提现状态 5：处理失败
     */
    public static final Byte WITHDRAWAL_STATE_FAIL = 5;

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

        /**
         * 鉴权失败，请重新登录
         */
        public static final String TOKEN_FAIL_ERROR_CODE = "1001";
    }

    class CashLog {
        /**
         * 金额类型--充值
         */
        public static final Byte AMOUNT_TYPE_DEPOSIT = 1;

        /**
         * 金额类型--提现
         */
        public static final Byte AMOUNT_TYPE_WITHDRAWAL = 2;

        /**
         * 金额类型--支付
         */
        public static final Byte AMOUNT_TYPE_PAY = 3;

        /**
         * 金额类型--赔付
         */
        public static final Byte AMOUNT_TYPE_PAYOUT = 4;

        /**
         * 金额类型--邀请赠送
         */
        public static final Byte AMOUNT_TYPE_INVITE = 5;

        /**
         * 金额类型--注册用户赠送
         */
        public static final Byte AMOUNT_TYPE_RIGESTER = 6;

        /**
         * 金额类型--提现--拒绝
         */
        public static final Byte AMOUNT_TYPE_WITHDRAWAL_REJECT = 7;

        /**
         * 金额类型--提现--打款失败
         */
        public static final Byte AMOUNT_TYPE_WITHDRAWAL_FAIL = 8;

        /**
         * 金额类型--收入
         */
        public static final Byte AMOUNT_TYPE_INCOME = 9;

        /**
         * 金额类型--支出
         */
        public static final Byte AMOUNT_TYPE_EXPENDITURE = 10;
    }

    /**
     * @author Lxl
     * @version V1.0 2018年6月6日
     */
    class Share {
        public static final String SHARE_TITLE = "ValPromise2.0";
        public static final String SHARE_CONTENT = "自由的金融交易平台";
        public static final String SHARE_ICON = "";// app默认显示logo图标
        public static final String SHARE_URL = "http://valp.io/?lang=zh-tw";
    }

    /**
     * 最小提现金额
     */
    public static final int WITHDRAWAL_AMOUNT_MIN = 1000;

    /**
     * 邀请客户赠送VPP数 30
     */
    public static final int INVITE_CUSTOMER_ADD_VPP = 30;
    /**
     * 注册赠送VPP数 30
     */
    public static final int REGISTER_CUSTOMER_ADD_VPP = 30;
}
