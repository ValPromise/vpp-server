/*
 * 文  件  名：WeatherTemp.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月24日
 * 修改内容：新增
 */
package com.vpp.core.standardized.trigger.bean;

/**
 * 天气温度数据bean
 * 
 * @author Lxl
 * @version V1.0 2018年5月24日
 */
public class WeatherTemp {
    private Float nmcMaxTemp;
    private Float cmaMaxTemp;
    @SuppressWarnings("unused")
    private Float realMaxTemp;

    public Float getNmcMaxTemp() {
        return nmcMaxTemp;
    }

    public void setNmcMaxTemp(Float nmcMaxTemp) {
        this.nmcMaxTemp = nmcMaxTemp;
    }

    public Float getCmaMaxTemp() {
        return cmaMaxTemp;
    }

    public void setCmaMaxTemp(Float cmaMaxTemp) {
        this.cmaMaxTemp = cmaMaxTemp;
    }

    public Float getRealMaxTemp() {
        return nmcMaxTemp != null ? nmcMaxTemp : cmaMaxTemp;
    }

}
