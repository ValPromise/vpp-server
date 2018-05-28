package com.vpp.common.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * app合约列表VO对象
 * 
 * @author Lxl
 * @version V1.0 2018年5月25日
 */
public class AppOrderInfoVo implements Serializable {
    private static final long serialVersionUID = 755875620889089544L;

    private String innerOrderId;// 订单
    private String status;// 状态
    private String cityName;// 城市名称
    private String stime;// 保障时间
    private String maxTemp;// 天气实况
    private String gmtCreate;// 创建时间
    private String weatherContent;// 合约履行标准
    private String contractContent;// 合约履行标准
    private BigDecimal maxPayout;// 合约预计收益
    private BigDecimal orderPrice;// 合约单价
    private Integer buyCount;// 合约购买份数
    private BigDecimal payFee;// 支付金额

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getContractContent() {
        return contractContent;
    }

    public void setContractContent(String contractContent) {
        this.contractContent = contractContent;
    }

    public BigDecimal getMaxPayout() {
        return maxPayout;
    }

    public void setMaxPayout(BigDecimal maxPayout) {
        this.maxPayout = maxPayout;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    public String getWeatherContent() {
        return weatherContent;
    }

    public void setWeatherContent(String weatherContent) {
        this.weatherContent = weatherContent;
    }

    @Override
    public String toString() {
        return "AppOrderInfoVo [innerOrderId=" + innerOrderId + ", status=" + status + ", cityName=" + cityName + ", stime="
                + stime + ", maxTemp=" + maxTemp + ", gmtCreate=" + gmtCreate + ", weatherContent=" + weatherContent
                + ", contractContent=" + contractContent + ", maxPayout=" + maxPayout + ", orderPrice=" + orderPrice
                + ", buyCount=" + buyCount + ", payFee=" + payFee + "]";
    }

}
