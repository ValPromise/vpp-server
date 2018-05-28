package com.vpp.common.mq.bean;

/**
 * 运营平台消息提醒bean
 * 
 * @author Lxl
 * @version V1.0 2018年1月7日
 */
public class SystemMessageMq {

    /**
     * 文本内容
     */
    private String message;

    /**
     * 消息级别 1：普通消息，2：告警，3：错误
     */
    private int level;

    /**
     * 播放提示音URL地址
     */
    private String voiceUrl;

    public SystemMessageMq(String message, int level, String voiceUrl) {
        super();
        this.message = message;
        this.level = level;
        this.voiceUrl = voiceUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

}
