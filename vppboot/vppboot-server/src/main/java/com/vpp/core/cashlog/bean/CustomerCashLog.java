package com.vpp.core.cashlog.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金流水
 * 
 * @author Lxl
 * @version V1.0 2018年6月6日
 */
public class CustomerCashLog {
    private Long id;

    private Long customerId;

    private Date gmtCreate;

    private Date gmtModified;

    private String description;

    private BigDecimal amount;

    private Byte amountType;

    private String amountSource;

    private BigDecimal balance;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Byte getAmountType() {
        return amountType;
    }

    public void setAmountType(Byte amountType) {
        this.amountType = amountType;
    }

    public String getAmountSource() {
        return amountSource;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setAmountSource(String amountSource) {
        this.amountSource = amountSource == null ? null : amountSource.trim();
    }

    @Override
    public String toString() {
        return "CustomerCashLog [id=" + id + ", customerId=" + customerId + ", gmtCreate=" + gmtCreate + ", gmtModified="
                + gmtModified + ", description=" + description + ", amount=" + amount + ", amountType=" + amountType
                + ", amountSource=" + amountSource + ", balance=" + balance + "]";
    }

}