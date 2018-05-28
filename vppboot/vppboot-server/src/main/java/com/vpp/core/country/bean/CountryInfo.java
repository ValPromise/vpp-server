package com.vpp.core.country.bean;

public class CountryInfo {
    private Long countryId;

    private String countryName;

    private String countryCode;

    private String description;

    private Long continentsId;

    private Integer sort;

    private String countryUs;

    private String countryCn;

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName == null ? null : countryName.trim();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getContinentsId() {
        return continentsId;
    }

    public void setContinentsId(Long continentsId) {
        this.continentsId = continentsId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCountryUs() {
        return countryUs;
    }

    public void setCountryUs(String countryUs) {
        this.countryUs = countryUs == null ? null : countryUs.trim();
    }

    public String getCountryCn() {
        return countryCn;
    }

    public void setCountryCn(String countryCn) {
        this.countryCn = countryCn == null ? null : countryCn.trim();
    }
}