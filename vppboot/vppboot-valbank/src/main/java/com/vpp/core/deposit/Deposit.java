package com.vpp.core.deposit;

import java.math.BigDecimal;
import java.util.Date;

public class Deposit {
    private Long id;

    private String cashNo;

    private BigDecimal amount;

    private Long customerId;

    private String fromAccount;

    private String toAccount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount == null ? null : fromAccount.trim();
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount == null ? null : toAccount.trim();
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