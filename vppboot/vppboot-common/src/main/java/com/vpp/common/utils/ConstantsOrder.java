package com.vpp.common.utils;

/**
 * 订单相关常量接口
 * 
 * @author Lxl
 * @version V1.0 2017年7月8日
 */
public interface ConstantsOrder {

    class Order {
        /**
         * 申请退款状态，未申请
         */
        public static final Byte REFUND_STATE_NO = 0;

        /**
         * 申请退款状态，已申请
         */
        public static final Byte REFUND_STATE_YES = 1;

    }

    class PolicyRefund {
        public static final Byte REFUND_STATE_NO = 0;// 待退款
        public static final Byte REFUND_STATE_YES = 1;// 已退款
        public static final Byte REFUND_STATE_OTHER = 2;// 其他
    }

    public static final Byte DATE_STATE_NO = 0;// 数据异常
    public static final Byte DATE_STATE_YES = 1;// 数据正常

    public static final Byte PAY_STATE_NO = 0;// 未支付
    public static final Byte PAY_STATE_YES = 1;// 已支付

    public static final Byte ORDER_STATE_NO = 0;// 异常
    public static final Byte ORDER_STATE_YES = 1;// 正常

    public static final Byte POLICY_STATE_NO = 0;// 异常
    public static final Byte POLICY_STATE_YES = 1;// 正常

    public static final Byte TRIGGER_STATE_NO = 0;// 未触发
    public static final Byte TRIGGER_STATE_YES = 1;// 已触发
    // public static final Byte TRIGGER_STATE_WAIT = 2;// 待触发

    public static final Byte PRODUCT_STATE_NO = 0;// 禁用，不销售
    public static final Byte PRODUCT_STATE_YES = 1;// 正常

    public static final Byte TRIGGER_DATA_STATE_NO = 2;// 异常触发数据
    public static final Byte TRIGGER_DATA_STATE_YES = 1;// 正常

    public static final Byte PAYTYPE_WX = 1;// //支付类型 微信
    public static final Byte PAYTYPE_ALI = 2;// 支付类型 支付宝

    public static final Byte PAYOUT_STATE_NO = 0;// 未赔付
    public static final Byte PAYOUT_STATE_YES = 1;// 已赔付
    public static final Byte PAYOUT_STATE_FAIL = 2;// 赔付失败
    public static final Byte PAYOUT_STATE_OVER = 3;// 赔付结束

    public static final Byte WX_STATE_YES = 0;// 已关注
    public static final Byte WX_STATE_NO = 1;// 取消关注

    public static final Byte WARN_NO = 0;// 0：未处理，
    public static final Byte WARN_YES = 1;// 1：已处理

    public static final Byte IS_OVER_TRIGGER_NO = 0;// 0：触发未结束
    public static final Byte IS_OVER_TRIGGER_YES = 1;// 1：触发已结束

    public static final Byte IS_OVER_PAYOUT_NO = 0;// 0：赔付未结束
    public static final Byte IS_OVER_PAYOUT_YES = 1;// 1：赔付已结束

    /**
     * 0：未做触发判断
     */
    public static final Byte TRIGGER_CHECK_STATE_NO = 0;//
    /**
     * 1：已做触发判断
     */
    public static final Byte TRIGGER_CHECK_STATE_YES = 1;//

    /**
     * 0：未取消
     */
    public static final Byte CANCEL_STATE_NO = 0;//
    /**
     * 1：已取消
     */
    public static final Byte CANCEL_STATE_YES = 1;

    public static final String TOKEN = "token";

    public static final String TEMPLATE_101 = "101";

    class Common {
        public static final String ERROR_OPERATION = "操作失败，请稍后再试。";
    }

    class Product {
        public static final String PRODUCT_ID_TEMP = "TQB001";
        public static final String PRODUCT_ID_RAIN = "TQB002";
    }
}
