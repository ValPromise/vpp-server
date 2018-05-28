package com.vpp.core.telegrambot;

import java.util.Date;

public class TelegramBot {
    private Long id;

    private String userWalletUrl;

    private String userWalletPublicKey;

    private String telegramUserId;

    private String telegramGroupId;

    private String convertCode;

    private Integer convertAmount;

    private String fromConvertCode;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer paymentConvertAmount;

    private Integer totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserWalletUrl() {
        return userWalletUrl;
    }

    public void setUserWalletUrl(String userWalletUrl) {
        this.userWalletUrl = userWalletUrl == null ? null : userWalletUrl.trim();
    }

    public String getUserWalletPublicKey() {
        return userWalletPublicKey;
    }

    public void setUserWalletPublicKey(String userWalletPublicKey) {
        this.userWalletPublicKey = userWalletPublicKey == null ? null : userWalletPublicKey.trim();
    }

    public String getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(String telegramUserId) {
        this.telegramUserId = telegramUserId == null ? null : telegramUserId.trim();
    }

    public String getTelegramGroupId() {
        return telegramGroupId;
    }

    public void setTelegramGroupId(String telegramGroupId) {
        this.telegramGroupId = telegramGroupId == null ? null : telegramGroupId.trim();
    }

    public String getConvertCode() {
        return convertCode;
    }

    public void setConvertCode(String convertCode) {
        this.convertCode = convertCode == null ? null : convertCode.trim();
    }

    public Integer getConvertAmount() {
        return convertAmount;
    }

    public void setConvertAmount(Integer convertAmount) {
        this.convertAmount = convertAmount;
    }

    public String getFromConvertCode() {
        return fromConvertCode;
    }

    public void setFromConvertCode(String fromConvertCode) {
        this.fromConvertCode = fromConvertCode == null ? null : fromConvertCode.trim();
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

    public Integer getPaymentConvertAmount() {
        return paymentConvertAmount;
    }

    public void setPaymentConvertAmount(Integer paymentConvertAmount) {
        this.paymentConvertAmount = paymentConvertAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
}