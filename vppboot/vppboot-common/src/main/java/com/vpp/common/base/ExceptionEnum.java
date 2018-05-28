package com.vpp.common.base;

public enum ExceptionEnum {
    /**
     * 系统超时，请稍后再试。
     */
    SYSTEM_TIME_OUT("100000", "系统超时，请稍后再试。");

    private String code;

    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
