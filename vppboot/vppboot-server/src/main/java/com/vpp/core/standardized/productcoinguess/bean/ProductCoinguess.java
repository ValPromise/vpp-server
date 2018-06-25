package com.vpp.core.standardized.productcoinguess.bean;

import java.math.BigDecimal;

public class ProductCoinguess {
    private Long id;

    private String productId;

    private Long customerId;

    private String startTime;

    private String endTime;

    private Byte productState;

    private String productName;

    private String description;

    private String dataSource;

    private BigDecimal minBet;

    private BigDecimal maxBet;

    private Integer orderAllowed;

    private BigDecimal singleTermLimit;

    private Integer riskControlTermLimit;

    private Integer lotteryInterval;

    private Float profitRate;

    private Integer priority;

    private Integer orderDeadline;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Byte getProductState() {
        return productState;
    }

    public void setProductState(Byte productState) {
        this.productState = productState;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource == null ? null : dataSource.trim();
    }

    public BigDecimal getMinBet() {
        return minBet;
    }

    public void setMinBet(BigDecimal minBet) {
        this.minBet = minBet;
    }

    public BigDecimal getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(BigDecimal maxBet) {
        this.maxBet = maxBet;
    }

    public Integer getOrderAllowed() {
        return orderAllowed;
    }

    public void setOrderAllowed(Integer orderAllowed) {
        this.orderAllowed = orderAllowed;
    }

    public BigDecimal getSingleTermLimit() {
        return singleTermLimit;
    }

    public void setSingleTermLimit(BigDecimal singleTermLimit) {
        this.singleTermLimit = singleTermLimit;
    }

    public Integer getRiskControlTermLimit() {
        return riskControlTermLimit;
    }

    public void setRiskControlTermLimit(Integer riskControlTermLimit) {
        this.riskControlTermLimit = riskControlTermLimit;
    }

    public Integer getLotteryInterval() {
        return lotteryInterval;
    }

    public void setLotteryInterval(Integer lotteryInterval) {
        this.lotteryInterval = lotteryInterval;
    }

    public Float getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(Float profitRate) {
        this.profitRate = profitRate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(Integer orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }
}