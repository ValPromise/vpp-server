package com.vpp.core.coinguess.bean;

import java.math.BigDecimal;

public class PriceInfo {
    private Boolean successGetPrice;

    private BigDecimal orderPrice;

    private BigDecimal actualPrice;

    private String actualLotteryTime;

    public Boolean getSuccessGetPrice() {
        return successGetPrice;
    }

    public void setSuccessGetPrice(Boolean successGetPrice) {
        this.successGetPrice = successGetPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getActualLotteryTime() {
        return actualLotteryTime;
    }

    public void setActualLotteryTime(String actualLotteryTime) {
        this.actualLotteryTime = actualLotteryTime;
    }
}
