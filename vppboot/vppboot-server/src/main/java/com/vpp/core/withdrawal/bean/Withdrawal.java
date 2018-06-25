package com.vpp.core.withdrawal.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Withdrawal {
    private Long id;

    private String cashNo;

    private BigDecimal vpp;

    private Long customerId;

    private String payerAddress;

    private String payeeAddress;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;

    private Byte state;

    private Long operatorId;

    private Date operationTime;

    private String operationDesc;

    private BigDecimal gas;

    /**
     * 区块链-时间戳
     */
    private Long bcTimeStamp;
    /**
     * 区块号
     */
    private Integer bcBlockNumber;
    /**
     * 区块链-事物回执
     */
    private String bcReceipt;

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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
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

    public BigDecimal getGas() {
        return gas;
    }

    public void setGas(BigDecimal gas) {
        this.gas = gas;
    }

    public Long getBcTimeStamp() {
        return bcTimeStamp;
    }

    public void setBcTimeStamp(Long bcTimeStamp) {
        this.bcTimeStamp = bcTimeStamp;
    }

    public Integer getBcBlockNumber() {
        return bcBlockNumber;
    }

    public void setBcBlockNumber(Integer bcBlockNumber) {
        this.bcBlockNumber = bcBlockNumber;
    }

    public String getBcReceipt() {
        return bcReceipt;
    }

    public void setBcReceipt(String bcReceipt) {
        this.bcReceipt = bcReceipt;
    }

    @Override
    public String toString() {
        return "Withdrawal [id=" + id + ", cashNo=" + cashNo + ", vpp=" + vpp + ", customerId=" + customerId + ", payerAddress="
                + payerAddress + ", payeeAddress=" + payeeAddress + ", description=" + description + ", gmtCreate=" + gmtCreate
                + ", gmtModified=" + gmtModified + ", state=" + state + ", operatorId=" + operatorId + ", operationTime="
                + operationTime + ", operationDesc=" + operationDesc + ", gas=" + gas + ", bcTimeStamp=" + bcTimeStamp
                + ", bcBlockNumber=" + bcBlockNumber + ", bcReceipt=" + bcReceipt + "]";
    }

}