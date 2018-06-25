package com.vpp.core.version.bean;

import java.util.Date;

public class Version {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String description;

    private String versionName;

    private String versionCode;

    // 版本序号，用于区分版本升级
    private Integer versionNo;

    private Date pushTime;

    private Long creater;

    private Long mender;

    private Byte isPush;

    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? null : versionName.trim();
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode == null ? null : versionCode.trim();
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getMender() {
        return mender;
    }

    public void setMender(Long mender) {
        this.mender = mender;
    }

    public Byte getIsPush() {
        return isPush;
    }

    public void setIsPush(Byte isPush) {
        this.isPush = isPush;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Override
    public String toString() {
        return "Version [id=" + id + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", description="
                + description + ", versionName=" + versionName + ", versionCode=" + versionCode + ", versionNo=" + versionNo
                + ", pushTime=" + pushTime + ", creater=" + creater + ", mender=" + mender + ", isPush=" + isPush + ", url="
                + url + "]";
    }

}