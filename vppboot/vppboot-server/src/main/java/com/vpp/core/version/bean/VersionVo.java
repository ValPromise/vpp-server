package com.vpp.core.version.bean;

import java.io.Serializable;

public class VersionVo implements Serializable{
	private static final long serialVersionUID = 7569824210638279974L;

	private Long id;

    private String gmtCreate;

    private String gmtModified;

    private String description;

    private String versionName;

    private String versionCode;

    private String pushTime;

    private Long creater;
    
    private String createrName;

    private Long mender;
    
    private String menderName;
    
    private Byte isPush;

    private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(String gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public Long getMender() {
		return mender;
	}

	public void setMender(Long mender) {
		this.mender = mender;
	}

	public String getMenderName() {
		return menderName;
	}

	public void setMenderName(String menderName) {
		this.menderName = menderName;
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
		this.url = url;
	}
}
