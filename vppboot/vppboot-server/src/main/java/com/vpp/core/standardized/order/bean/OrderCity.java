package com.vpp.core.standardized.order.bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderCity {
    private Long id;

    private String innerOrderId;

    private String cityId;

    private Float threshold;

    private Date stime;

    private Date etime;

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

    public Date getStime() {
        return stime;
    }

    public void setStime(Date stime) {
        this.stime = stime;
    }

    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
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
}