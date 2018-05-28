package com.vpp.common.vo;

import java.io.Serializable;

/**
 * app合约列表VO对象
 * 
 * @author Lxl
 * @version V1.0 2018年5月25日
 */
public class AppOrderListVo implements Serializable {
    private static final long serialVersionUID = 755875620889089544L;
    /**
     * 上海0201 2018-05-11 22:11:12 最高温度>5.5℃ 15-100 等待判定
     */
    private String innerOrderId;
    private String title;
    private String gmtCreate;
    private String content;
    private String amount;
    private String status;

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderAppListVo [innerOrderId=" + innerOrderId + ", title=" + title + ", gmtCreate=" + gmtCreate + ", content="
                + content + ", amount=" + amount + ", status=" + status + "]";
    }
    
}
