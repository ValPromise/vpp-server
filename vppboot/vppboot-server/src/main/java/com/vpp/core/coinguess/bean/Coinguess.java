package com.vpp.core.coinguess.bean;

import java.math.BigDecimal;

public class Coinguess {
    private Long id;

    private Long customerId;

    private String orderId;

    private String targetId;

    private BigDecimal orderAmt;

    private Integer orderDir;

    private String orderTs;

    private String orderTsUnix;

    private BigDecimal orderPrice;

    private BigDecimal profit;

    private String lotteryTime;

    private String lotteryTimeUnix;

    private String actualLotteryTime;

    private String actualLotteryTimeUnix;

    private BigDecimal actualPrice;

    private Integer status;

    private Integer rewardFlg;

    private String description;

    private String gmtCreate;

    private String gmtModified;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Integer getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(Integer orderDir) {
        this.orderDir = orderDir;
    }

    public String getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(String orderTs) {
        this.orderTs = orderTs;
    }

    public String getOrderTsUnix() {
        return orderTsUnix;
    }

    public void setOrderTsUnix(String orderTsUnix) {
        this.orderTsUnix = orderTsUnix == null ? null : orderTsUnix.trim();
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public String getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(String lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public String getLotteryTimeUnix() {
        return lotteryTimeUnix;
    }

    public void setLotteryTimeUnix(String lotteryTimeUnix) {
        this.lotteryTimeUnix = lotteryTimeUnix == null ? null : lotteryTimeUnix.trim();
    }

    public String getActualLotteryTime() {
        return actualLotteryTime;
    }

    public void setActualLotteryTime(String actualLotteryTime) {
        this.actualLotteryTime = actualLotteryTime;
    }

    public String getActualLotteryTimeUnix() {
        return actualLotteryTimeUnix;
    }

    public void setActualLotteryTimeUnix(String actualLotteryTimeUnix) {
        this.actualLotteryTimeUnix = actualLotteryTimeUnix == null ? null : actualLotteryTimeUnix.trim();
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRewardFlg() {
        return rewardFlg;
    }

    public void setRewardFlg(Integer rewardFlg) {
        this.rewardFlg = rewardFlg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
