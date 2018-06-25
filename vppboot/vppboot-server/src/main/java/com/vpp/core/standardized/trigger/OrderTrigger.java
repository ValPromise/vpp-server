package com.vpp.core.standardized.trigger;

import java.math.BigDecimal;

import com.vpp.common.utils.DateUtil;

public class OrderTrigger {
    private Long triggerId;

    private String cityId;

    private BigDecimal payoutFee;

    private Byte triggerState;

    private String gmtCreate;

    private String description;

    private Byte dataState;

    private Float threshold;

    private String realWeatherDate;

    private Float nmcWeatherValue;

    private Float cmaWeatherValue;

    private String innerOrderId;

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public BigDecimal getPayoutFee() {
        return payoutFee;
    }

    public void setPayoutFee(BigDecimal payoutFee) {
        this.payoutFee = payoutFee;
    }

    public Byte getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(Byte triggerState) {
        this.triggerState = triggerState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getDataState() {
        return dataState;
    }

    public void setDataState(Byte dataState) {
        this.dataState = dataState;
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public String getGmtCreate() {
        return DateUtil.removeS(gmtCreate);
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getRealWeatherDate() {
        return DateUtil.removeS(realWeatherDate);
    }

    public void setRealWeatherDate(String realWeatherDate) {
        this.realWeatherDate = realWeatherDate;
    }

    public Float getNmcWeatherValue() {
        return nmcWeatherValue;
    }

    public void setNmcWeatherValue(Float nmcWeatherValue) {
        this.nmcWeatherValue = nmcWeatherValue;
    }

    public Float getCmaWeatherValue() {
        return cmaWeatherValue;
    }

    public void setCmaWeatherValue(Float cmaWeatherValue) {
        this.cmaWeatherValue = cmaWeatherValue;
    }

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId == null ? null : innerOrderId.trim();
    }
}