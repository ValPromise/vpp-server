package com.vpp.core.app.trigger.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.rain.service.IRainOrderTriggerService;
import com.vpp.core.standardized.trigger.ITriggerService;

/**
 * app降雨合约查询
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
@RestController
@RequestMapping("/app/trigger")
public class AppTriggerController extends CommonController {
    @Autowired
    private ITriggerService triggerService;
    @Autowired
    private IRainOrderTriggerService rainOrderTriggerService;

    @RequestMapping(value = "/rain")
    @ResponseBody
    public ResultVo rain(String token, String etime, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        if (StringUtils.isBlank(etime)) {
            return ResultVo.setResultError(getMessage("parameter_error"));
        }

        Date dd = DateUtil.parse(etime, DateUtil.YMD_DATE_PATTERN);
        etime = DateUtil.format(dd, DateUtil.YMD_DATE_PATTERN);

        rainOrderTriggerService.triggerByEtime(etime);
        // triggerService.triggerByEtime(etime);
        return ResultVo.setResultSuccess();
    }

    @RequestMapping(value = "/temp")
    @ResponseBody
    public ResultVo temp(String token, String etime, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        if (StringUtils.isBlank(etime)) {
            return ResultVo.setResultError(getMessage("parameter_error"));
        }
        Date dd = DateUtil.parse(etime, DateUtil.YMD_DATE_PATTERN);
        etime = DateUtil.format(dd, DateUtil.YMD_DATE_PATTERN);

        triggerService.triggerByEtime(etime);
        return ResultVo.setResultSuccess();
    }
}
