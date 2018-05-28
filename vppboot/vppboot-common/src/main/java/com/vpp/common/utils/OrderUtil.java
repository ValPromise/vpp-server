package com.vpp.common.utils;


public class OrderUtil {
    // public static void initOrderParamsByProduct(OrderList orderList, ProductConfig product) {
    // orderList.setContractId("");// 合约ID
    // orderList.setTemplateId(product.getTemplateId());
    // orderList.setMerchantId(product.getMerchantId());
    // orderList.setProductId(product.getProductId());
    // orderList.setOpType(product.getOpType());// 判断：1-小与;2-小于或等于;3-等于;4-大于或等于;5-大于
    // orderList.setWeatherType(product.getWeatherType());// 天气类型
    // orderList.setPayState(ConstantsOrder.PAY_STATE_NO);// 支付状态
    // orderList.setOrderState(ConstantsOrder.ORDER_STATE_YES);// 订单状态
    // orderList.setTriggerState(ConstantsOrder.TRIGGER_STATE_NO);// 触发状态
    // orderList.setIsOverTrigger(ConstantsOrder.IS_OVER_TRIGGER_NO);// 触发未结束
    // orderList.setIsOverPayout(ConstantsOrder.IS_OVER_PAYOUT_NO);// 赔付未结束
    // orderList.setRefundState(ConstantsOrder.Order.REFUND_STATE_NO);// 退款状态
    // orderList.setCancelState(ConstantsOrder.CANCEL_STATE_NO);// 取消状态
    // orderList.setTriggerCheckState(ConstantsOrder.TRIGGER_CHECK_STATE_NO);// 触发判断状态 0：否
    // orderList.setSettlementType(product.getSettlementType());// 结算类型
    // orderList.setPaymentType(product.getPaymentType());// 付款类型，先付/后付
    // orderList.setShareRate(product.getShareRate());// 分成比例
    // orderList.setGmtCreate(DateUtil.getCurrentDateTimeLocal());// 创建时间
    // orderList.setBuyCount(Byte.valueOf((byte) 1));// 购买数量
    // }

    /**
     * 创建唯一订单编号
     * 
     * @author Lxl
     * @param prefix 前缀
     * @return
     */
    public static String createId(String prefix) {
        String dealId = String.valueOf(System.nanoTime());
        dealId = dealId.substring(dealId.length() - 9, dealId.length() - 1);
        int endRandom = (int) (Math.random() * 9000 + 1000);
        dealId = prefix + dealId + endRandom;

        return dealId.replaceFirst("0", "1");
    }

    /**
     * TODO 触发值和实况值对比
     * 
     * @author cgp
     * @param realWeather 实况值
     * @param threshold 触发值
     * @param opType 判断类型
     * @return
     */
    public static boolean weatherCompare(Float realWeather, Float threshold, int opType) {
        boolean compare = false;
        // nmc的值和触发值比 '1:小与;2:小于或等于;3:等于;4:大于或等于;5:大于',
        switch (opType) {
        case 1:// 实况值小于触发值
            compare = realWeather.compareTo(threshold) < 0;
            break;
        case 2:// 实况值小于等于触发值
            compare = realWeather.compareTo(threshold) <= 0;
            break;
        case 3:// 实况值等于触发值
            compare = realWeather.compareTo(threshold) == 0;
            break;
        case 4:// 实况值大于等于触发值
            compare = realWeather.compareTo(threshold) >= 0;
            break;
        case 5:// 实况值大于触发值
            compare = realWeather.compareTo(threshold) > 0;
            break;
        }
        return compare;
    }
}
