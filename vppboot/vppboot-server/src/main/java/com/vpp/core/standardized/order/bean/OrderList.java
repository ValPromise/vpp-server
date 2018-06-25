package com.vpp.core.standardized.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.vpp.common.utils.DateUtil;

public class OrderList implements Serializable {
    private static final long serialVersionUID = 4083727217285853864L;

    private Long id;

    private Long customerId;

    private String innerOrderId;

    private String productId;

    private String templateId;

    private String gmtCreate;

    private Integer buyCount;

    private BigDecimal orderPrice;

    private BigDecimal payFee;

    private String payTime;

    private String payReceipt;

    private BigDecimal payoutFee;

    private BigDecimal maxPayout;

    private BigDecimal realPayoutFee;

    private String stime;

    private String etime;

    private Byte opType;

    private Byte weatherType;

    private String triggerRuleParam;

    private String payoutRuleParam;

    private Byte payState;

    private Byte orderState;

    private Byte triggerCheckState;

    private String orderDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId == null ? null : innerOrderId.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    public String getPayReceipt() {
        return payReceipt;
    }

    public void setPayReceipt(String payReceipt) {
        this.payReceipt = payReceipt == null ? null : payReceipt.trim();
    }

    public BigDecimal getPayoutFee() {
        return payoutFee;
    }

    public void setPayoutFee(BigDecimal payoutFee) {
        this.payoutFee = payoutFee;
    }

    public BigDecimal getMaxPayout() {
        return maxPayout;
    }

    public void setMaxPayout(BigDecimal maxPayout) {
        this.maxPayout = maxPayout;
    }

    public BigDecimal getRealPayoutFee() {
        return realPayoutFee;
    }

    public void setRealPayoutFee(BigDecimal realPayoutFee) {
        this.realPayoutFee = realPayoutFee;
    }

    public Byte getOpType() {
        return opType;
    }

    public void setOpType(Byte opType) {
        this.opType = opType;
    }

    public Byte getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(Byte weatherType) {
        this.weatherType = weatherType;
    }

    public String getTriggerRuleParam() {
        return triggerRuleParam;
    }

    public void setTriggerRuleParam(String triggerRuleParam) {
        this.triggerRuleParam = triggerRuleParam == null ? null : triggerRuleParam.trim();
    }

    public String getPayoutRuleParam() {
        return payoutRuleParam;
    }

    public void setPayoutRuleParam(String payoutRuleParam) {
        this.payoutRuleParam = payoutRuleParam == null ? null : payoutRuleParam.trim();
    }

    public Byte getPayState() {
        return payState;
    }

    public void setPayState(Byte payState) {
        this.payState = payState;
    }

    public Byte getOrderState() {
        return orderState;
    }

    public void setOrderState(Byte orderState) {
        this.orderState = orderState;
    }

    public Byte getTriggerCheckState() {
        return triggerCheckState;
    }

    public void setTriggerCheckState(Byte triggerCheckState) {
        this.triggerCheckState = triggerCheckState;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc == null ? null : orderDesc.trim();
    }

    public String getGmtCreate() {
        return DateUtil.removeS(gmtCreate);
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getPayTime() {
        return DateUtil.removeS(payTime);
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getStime() {
        return DateUtil.removeS(stime);
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return DateUtil.removeS(etime);
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    @Override
    public String toString() {
        return "OrderList [id=" + id + ", customerId=" + customerId + ", innerOrderId=" + innerOrderId + ", productId="
                + productId + ", templateId=" + templateId + ", gmtCreate=" + gmtCreate + ", buyCount=" + buyCount
                + ", orderPrice=" + orderPrice + ", payFee=" + payFee + ", payTime=" + payTime + ", payReceipt=" + payReceipt
                + ", payoutFee=" + payoutFee + ", maxPayout=" + maxPayout + ", realPayoutFee=" + realPayoutFee + ", stime="
                + stime + ", etime=" + etime + ", opType=" + opType + ", weatherType=" + weatherType + ", triggerRuleParam="
                + triggerRuleParam + ", payoutRuleParam=" + payoutRuleParam + ", payState=" + payState + ", orderState="
                + orderState + ", triggerCheckState=" + triggerCheckState + ", orderDesc=" + orderDesc + "]";
    }

}