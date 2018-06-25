package com.vpp.service.city.bean;

import java.io.Serializable;

/**
 * city bean
 * 
 * @author Lxl
 * @version V1.0 2018年5月28日
 */
public class CityVo implements Serializable {
    private static final long serialVersionUID = 755875620881289544L;
    private String cityId;
    private String cnName;
    /**
     * 首字母
     */
    private String initial;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    @Override
    public String toString() {
        return "AppCityVo [cityId=" + cityId + ", cnName=" + cnName + ", initial=" + initial + "]";
    }

}
