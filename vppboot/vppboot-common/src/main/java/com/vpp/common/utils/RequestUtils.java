/*
 * 文  件  名：RequestUtils.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月24日
 * 修改内容：新增
 */
package com.vpp.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO 添加类的描述
 * 
 * @author Lxl
 * @version V1.0 2018年5月24日
 */
public class RequestUtils {
    /**
     * 获取请求IP
     * 
     * @author Lxl
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();
        return ip;
    }

}
