/*
 * 文  件  名：WeatherTemp.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月24日
 * 修改内容：新增
 */
package com.vpp.core.weather.bean;

/**
 * 天气温度数据bean
 * 
 * @author Lxl
 * @version V1.0 2018年5月24日
 */
public class WeatherData {
    /**
     * nmc最高温度
     */
    private Float nmcMaxTemp;
    /**
     * cma最高温度
     */
    private Float cmaMaxTemp;
    /**
     * 输出最高温度 优先取NMC数据
     */
    @SuppressWarnings("unused")
    private Float realMaxTemp;

    /**
     * cma降雨
     */
    private Float cmaPrcp;
    /**
     * NMC降雨
     */
    private Float nmcPrcp;
    /**
     * 输出降雨量，优先取nmc降雨数据
     */
    @SuppressWarnings("unused")
    private Float realPrcp;

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

    public Float getCmaPrcp() {
        return cmaPrcp;
    }

    public void setCmaPrcp(Float cmaPrcp) {
        this.cmaPrcp = cmaPrcp;
    }

    public Float getNmcPrcp() {
        return nmcPrcp;
    }

    public void setNmcPrcp(Float nmcPrcp) {
        this.nmcPrcp = nmcPrcp;
    }

    public Float getRealPrcp() {
        return nmcPrcp != null ? nmcPrcp : cmaPrcp;
    }
}
