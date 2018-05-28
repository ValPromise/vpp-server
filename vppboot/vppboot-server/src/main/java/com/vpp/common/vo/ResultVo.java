package com.vpp.common.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResultVo implements Serializable {
    /**
     * success
     */
    static final String SUCCESS = "success";

    /**
     * error
     */
    static final String ERROR = "error";

    /**
     * page
     */
    static final String PAGE = "page";

    public static final Integer STATE_SUCCESS = 1;// 成功
    public static final Integer STATE_ERROR = 0;// 失败

    private static final long serialVersionUID = 331807477223856823L;

    private Integer state;

    private String message;

    private String errorCode;//

    @SuppressWarnings("unused")
    private Long serverTime;

    private Object data;

    private static final String TIMEOUT = "系统繁忙，请稍后再试。";

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public static ResultVo setResultSuccess() {
        ResultVo result = new ResultVo();
        result.setState(STATE_SUCCESS);
        return result;
    }

    public static ResultVo setResultSuccess(Object data) {
        ResultVo result = new ResultVo();
        result.setState(STATE_SUCCESS);
        result.setData(data);
        return result;
    }

    public static ResultVo setResultSuccess(String message, Object data) {
        ResultVo result = new ResultVo();
        result.setState(STATE_SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    public static ResultVo setResultSuccessMsg(String message) {
        ResultVo result = new ResultVo();
        result.setState(STATE_SUCCESS);
        result.setMessage(message); 
        return result;
    }

    public static ResultVo setResultError(String message) {
        ResultVo result = new ResultVo();
        result.setState(STATE_ERROR);
        result.setMessage(message);
        return result;
    }

    public static ResultVo setResultError() {
        ResultVo result = new ResultVo();
        result.setState(STATE_ERROR);
        result.setMessage(TIMEOUT);
        return result;
    }

    public static ResultVo setResultError(String message, String errorCode) {
        ResultVo result = new ResultVo();
        result.setState(STATE_ERROR);
        result.setMessage(message);
        result.setErrorCode(errorCode);
        return result;
    }

    public static ResultVo setResultError(String message, Object data) {
        ResultVo result = new ResultVo();
        result.setState(STATE_ERROR);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Long getServerTime() {
        return System.currentTimeMillis();
    }
}