package com.vpp.common.utils;

/**
 * MQ常量
 * 
 * @author Lxl
 * @version V1.0 2017年12月18日
 */
public interface ConstantsMq {

    /**
     * 消息推送
     */
    public static String MESSAGE_COMMON = "message_common";

    /**
     * 运营平台语音提醒
     */
    public static String SYSTEM_INFO = "system_info";

    /**
     * 订单购买成功语音文件
     */
    public static final String VOICE_URL = "http://yss.yisell.com/yisell/ycys2017030818030888/sound/yisell_sound_2014080715271977634_66366.mp3";

    /**
     * 信息级别 1：普通信息
     */
    public static Integer LEVEL_INFO = 1;

    /**
     * 信息级别 2：告警
     */
    public static Integer LEVEL_WARN = 2;

    /**
     * 信息级别 3：错误
     */
    public static Integer LEVEL_ERROR = 3;
}
