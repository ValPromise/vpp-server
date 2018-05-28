package com.vpp.core.vppkyc;

import java.util.Date;

public class VppKyc {
    private Long id;

    private String userName;

    private String nation;

    private String email;

    private String mobile;

    private String wechat;

    private String telegramId;

    private String investProjects;

    private String institutionName;

    private String passportImgUrl;

    private String institutionImgUrl;

    private String walletAccount;

    private String investmentAmounts;

    private Date gmtTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId == null ? null : telegramId.trim();
    }

    public String getInvestProjects() {
        return investProjects;
    }

    public void setInvestProjects(String investProjects) {
        this.investProjects = investProjects == null ? null : investProjects.trim();
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName == null ? null : institutionName.trim();
    }

    public String getPassportImgUrl() {
        return passportImgUrl;
    }

    public void setPassportImgUrl(String passportImgUrl) {
        this.passportImgUrl = passportImgUrl == null ? null : passportImgUrl.trim();
    }

    public String getInstitutionImgUrl() {
        return institutionImgUrl;
    }

    public void setInstitutionImgUrl(String institutionImgUrl) {
        this.institutionImgUrl = institutionImgUrl == null ? null : institutionImgUrl.trim();
    }

    public String getWalletAccount() {
        return walletAccount;
    }

    public void setWalletAccount(String walletAccount) {
        this.walletAccount = walletAccount == null ? null : walletAccount.trim();
    }

    public String getInvestmentAmounts() {
        return investmentAmounts;
    }

    public void setInvestmentAmounts(String investmentAmounts) {
        this.investmentAmounts = investmentAmounts == null ? null : investmentAmounts.trim();
    }

    public Date getGmtTime() {
        return gmtTime;
    }

    public void setGmtTime(Date gmtTime) {
        this.gmtTime = gmtTime;
    }
}