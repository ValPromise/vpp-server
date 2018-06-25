
package com.vpp.common.vo;

import java.io.Serializable;
/**
 * Coinguess 订单列表VO对象
 *
 * @author shiming
 * @version V1.0 2018年6月14日
 */
public class CoinguessOrderListVo implements Serializable {
    private static final long serialVersionUID = 755875620882229544L;
    /**
     * 上海0201 2018-05-11 22:11:12 最高温度>5.5℃ 15-100 等待判定
     */
    private String innerOrderId;   //订单id
    private String title;   //产品ID
    private String gmtCreate; //订单生成时间（秒）
    private String content; //看涨/看跌
    private String amount; //订单金额 100-180
    private String status; //等待判定、已判定，无需履行、已判定，已履行
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
