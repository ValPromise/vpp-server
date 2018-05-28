/*
 * 文  件  名：ObjectUtils.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月16日
 * 修改内容：新增
 */
package com.vpp.common.utils;

/**
 * TODO 添加类的描述
 * 
 * @author Lxl
 * @version V1.0 2018年5月16日
 */
public class ObjectUtils {
    /**
     * 空判断
     * 
     * @author Lxl
     * @param params
     * @return
     */
    public static boolean isNotNull(Object... params) {
        for (Object param : params) {
            if (null == param) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNull(Object... params) {
        for (Object param : params) {
            if (null == param) {
                return true;
            }
        }
        return false;
    }
}
