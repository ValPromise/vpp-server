package com.vpp.core.standardized.payout;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPayout {
    private Long id;

    private Date gmtCreate;

    private String description;

    private BigDecimal payoutFee;

    private Byte payoutState;

    private Long customerId;

    private Long operatorId;

    private Date operationTime;

    private String operationDesc;

    private BigDecimal realPayoutFee;

    private String payReceipt;

    private Byte dataState;

    private String innerOrderId;

    private String triggerCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getPayoutFee() {
        return payoutFee;
    }

    public void setPayoutFee(BigDecimal payoutFee) {
        this.payoutFee = payoutFee;
    }

    public Byte getPayoutState() {
        return payoutState;
    }

    public void setPayoutState(Byte payoutState) {
        this.payoutState = payoutState;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc == null ? null : operationDesc.trim();
    }

    public BigDecimal getRealPayoutFee() {
        return realPayoutFee;
    }

    public void setRealPayoutFee(BigDecimal realPayoutFee) {
        this.realPayoutFee = realPayoutFee;
    }

    public String getPayReceipt() {
        return payReceipt;
    }

    public void setPayReceipt(String payReceipt) {
        this.payReceipt = payReceipt == null ? null : payReceipt.trim();
    }

    public Byte getDataState() {
        return dataState;
    }

    public void setDataState(Byte dataState) {
        this.dataState = dataState;
    }

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId == null ? null : innerOrderId.trim();
    }

    public String getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(String triggerCode) {
        this.triggerCode = triggerCode == null ? null : triggerCode.trim();
    }
}