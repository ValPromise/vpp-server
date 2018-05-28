package com.vpp.common.utils;

public class OpTypeUtils {
    // 1-小与;2-小于或等于;3-等于;4-大于或等于;5-大于
    /**
     * 获取判断类型符号
     * 
     * @author Lxl
     * @param opType
     * @return
     */
    public static String getSymbol(Byte opType) {
        if (1 == opType.intValue()) {
            return "<";
        } else if (2 == opType.intValue()) {
            return "<=";
        } else if (3 == opType.intValue()) {
            return "=";
        } else if (4 == opType.intValue()) {
            return ">=";
        } else if (5 == opType.intValue()) {
            return ">";
        }
        return "";
    }

    /**
     * 获取判断类型符号 中文
     * 
     * @author Lxl
     * @param opType
     * @return
     */
    public static String getSymbolChinese(Byte opType) {
        if (1 == opType.intValue()) {
            return "小与";
        } else if (2 == opType.intValue()) {
            return "小于或等于";
        } else if (3 == opType.intValue()) {
            return "等于";
        } else if (4 == opType.intValue()) {
            return "大于或等于";
        } else if (5 == opType.intValue()) {
            return "大于";
        }
        return "";
    }
}
