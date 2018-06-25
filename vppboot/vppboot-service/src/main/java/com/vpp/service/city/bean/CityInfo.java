package com.vpp.service.city.bean;

import java.io.Serializable;

public class CityInfo implements Serializable {
    private static final long serialVersionUID = -2635331364782350833L;

    private Long id;

    private String cityIdOld;

    private String cityId;

    private String enName;

    private String cnName;

    private String pinyin;

    private Long provinceId;

    private String area;

    private String longitude;

    private String latitude;

    private Long countryId;

    private String weatherUrl;

    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityIdOld() {
        return cityIdOld;
    }

    public void setCityIdOld(String cityIdOld) {
        this.cityIdOld = cityIdOld == null ? null : cityIdOld.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName == null ? null : cnName.trim();
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl == null ? null : weatherUrl.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "CityInfo [id=" + id + ", cityIdOld=" + cityIdOld + ", cityId=" + cityId + ", enName=" + enName + ", cnName="
                + cnName + ", pinyin=" + pinyin + ", provinceId=" + provinceId + ", area=" + area + ", longitude=" + longitude
                + ", latitude=" + latitude + ", countryId=" + countryId + ", weatherUrl=" + weatherUrl + ", sort=" + sort + "]";
    }

}