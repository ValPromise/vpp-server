package com.vpp.core.deposit.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Deposit {
    private Long id;

    private String cashNo;

    private BigDecimal vpp;

    private Long customerId;

    private String payerAddress;

    private String payeeAddress;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;

    private Long timastamp;

    private Long fromBlock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCashNo() {
        return cashNo;
    }

    public void setCashNo(String cashNo) {
        this.cashNo = cashNo == null ? null : cashNo.trim();
    }

    public BigDecimal getVpp() {
        return vpp;
    }

    public void setVpp(BigDecimal vpp) {
        this.vpp = vpp;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress == null ? null : payerAddress.trim();
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(String payeeAddress) {
        this.payeeAddress = payeeAddress == null ? null : payeeAddress.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getTimastamp() {
        return timastamp;
    }

    public void setTimastamp(Long timastamp) {
        this.timastamp = timastamp;
    }

    public Long getFromBlock() {
        return fromBlock;
    }

    public void setFromBlock(Long fromBlock) {
        this.fromBlock = fromBlock;
    }
}