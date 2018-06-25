package com.vpp.core.standardized.productcoinguess.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ProductCoinguessList {
    private String productId;

    private String dataSource;

    private BigDecimal minBet;

    private BigDecimal maxBet;

    private Integer lotteryInterval;

    private Float profitRate;

    private Integer orderDeadline;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public Integer getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(Integer orderDeadline) {
        this.orderDeadline = orderDeadline;
    }
}