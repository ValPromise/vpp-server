package com.vpp.common.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.vpp.common.utils.DateUtil;

public class OrderInfoVo implements Serializable {
    private static final long serialVersionUID = 755875620889089564L;

    private Long id;

    private Long customerId;

    private String innerOrderId;

    private String productId;

    private String templateId;

    private String gmtCreate;

    private Integer buyCount;

    private BigDecimal orderPrice;

    private BigDecimal payFee;

    private Date payTime;

    private String payReceipt;

    private BigDecimal payoutFee;

    private BigDecimal maxPayout;

    private BigDecimal realPayoutFee;

    private Byte payState;

    private Byte orderState;

    private Byte triggerCheckState;

    private String orderDesc;

    private String cityId;

    private String cityCnName;
    private String cityEnName;

    private Float threshold;

    private String stime;

    private String etime;

    private Byte opType;

    private Byte weatherType;

    private BigDecimal contractPrice;

    private String contractId;

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
        this.innerOrderId = innerOrderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getGmtCreate() {
        return DateUtil.removeS(gmtCreate);
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayReceipt() {
        return payReceipt;
    }

    public void setPayReceipt(String payReceipt) {
        this.payReceipt = payReceipt;
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
        this.orderDesc = orderDesc;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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
        this.contractId = contractId;
    }

    public String getCityCnName() {
        return cityCnName;
    }

    public void setCityCnName(String cityCnName) {
        this.cityCnName = cityCnName;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    @Override
    public String toString() {
        return "OrderInfoVo [id=" + id + ", customerId=" + customerId + ", innerOrderId=" + innerOrderId + ", productId="
                + productId + ", templateId=" + templateId + ", gmtCreate=" + gmtCreate + ", buyCount=" + buyCount
                + ", orderPrice=" + orderPrice + ", payFee=" + payFee + ", payTime=" + payTime + ", payReceipt=" + payReceipt
                + ", payoutFee=" + payoutFee + ", maxPayout=" + maxPayout + ", realPayoutFee=" + realPayoutFee + ", payState="
                + payState + ", orderState=" + orderState + ", triggerCheckState=" + triggerCheckState + ", orderDesc="
                + orderDesc + ", cityId=" + cityId + ", cityCnName=" + cityCnName + ", cityEnName=" + cityEnName
                + ", threshold=" + threshold + ", stime=" + stime + ", etime=" + etime + ", opType=" + opType + ", weatherType="
                + weatherType + ", contractPrice=" + contractPrice + ", contractId=" + contractId + "]";
    }

    
}
