package com.vpp.common.utils;


/**
 * 错误工具
 * 
 * @author Lxl
 * @version V1.0 2017年8月14日
 */
public interface ErrorCode {

    public enum TqbCode {
        C1050("1050", "鉴权失败"), C1051("1051", "登录失败"), C1052("1052", "超时"), C5001("5001", "业务异常，请重新获取业务信息"),
        E1050("1050","登录超时,请重新登录!");
        private String code;
        private String msg;

        private TqbCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static String getResponseMsg(String code) {
            for (TqbCode tqbCode : TqbCode.values()) {
                if (code.equals(tqbCode.getCode())) {
                    return tqbCode.getMsg();
                }
            }
            return "";
        }
    }

    /**
     * 风控错误编码字典
     * 
     * @author THINK
     */
    public enum RiskCode {

        /**
         * 风控：单用户，单产品，单城市的保障日期不能重叠
         */
        DEAL_RISK_6001("6001", "您要购买的产品保障日期和已购买有重复"),
        /**
         * 单用户，单产品，一天能购买的订单数上限（后续可以调整）
         */
        DEAL_RISK_6002("6002", "您今天已购买得太多啦"),
        /**
         * 单产品单日最大总购买金额上限为1万元
         */
        DEAL_RISK_6003("6003", "抱歉，该产品已被抢购一空"),
        /**
         * 单用户，单产品，单日购买金额上限（后续可以调整）
         */
        DEAL_RISK_6004("6004", "您今天已购买得太多啦"),
        /**
         * 单用户，单渠道，单约赔付金额上限（后续可以调整）
         */
        DEAL_RISK_6005("6005", "您本月已购买得太多啦"),
        /**
         * 产品已售完，请选择其他产品
         */
        DEAL_RISK_6010("6010", "产品已售完，请选择其他产品。");

        private String code;

        private String msg;

        private RiskCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static String getResponseMsg(String code) {
            for (RiskCode errorCode : RiskCode.values()) {
                if (code.equals(errorCode.getCode())) {
                    return errorCode.getMsg();
                }
            }
            return "";
        }
    }

}
