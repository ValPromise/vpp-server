package com.vpp.core.standardized.order.bean;

import java.io.Serializable;

/**
 * 合约查询封装bean
 * 
 * @author Lxl
 * @version V1.0 2018年5月28日
 */
public class DataModelDto implements Serializable {

    private static final long serialVersionUID = -2197993766408585467L;

    private String innerOrderId;

    private String cityId;

    private String stime;

    private String etime;

    private Float threshold;

    private Integer price;

    private Integer payoutFee;

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId;
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPayoutFee() {
        return payoutFee;
    }

    public void setPayoutFee(Integer payoutFee) {
        this.payoutFee = payoutFee;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    @Override
    public String toString() {
        return "DataModelDto [innerOrderId=" + innerOrderId + ", cityId=" + cityId + ", stime=" + stime + ", etime=" + etime
                + ", threshold=" + threshold + ", price=" + price + ", payoutFee=" + payoutFee + "]";
    }

}