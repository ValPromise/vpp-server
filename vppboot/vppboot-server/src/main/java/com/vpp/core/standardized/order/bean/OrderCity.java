package com.vpp.core.standardized.order.bean;

import java.math.BigDecimal;

import com.vpp.common.utils.DateUtil;

public class OrderCity {
    private Long id;

    private String innerOrderId;

    private String cityId;

    private Float threshold;

    private String stime;

    private String etime;

    private Byte opType;

    private Byte weatherType;

    private String triggerRuleParam;

    private String payoutRuleParam;

    private BigDecimal contractPrice;

    private String contractId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId == null ? null : innerOrderId.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
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

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    @Override
    public String toString() {
        return "OrderCity [id=" + id + ", innerOrderId=" + innerOrderId + ", cityId=" + cityId + ", threshold=" + threshold
                + ", stime=" + stime + ", etime=" + etime + ", opType=" + opType + ", weatherType=" + weatherType
                + ", triggerRuleParam=" + triggerRuleParam + ", payoutRuleParam=" + payoutRuleParam + ", contractPrice="
                + contractPrice + ", contractId=" + contractId + "]";
    }

}