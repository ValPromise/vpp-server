package com.vpp.core.app.version.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.version.bean.Version;
import com.vpp.core.version.service.IVersionService;

/**
 * 版本比对接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月30日
 */
@RestController
@RequestMapping("/app/version/")
public class AppVersionController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppVersionController.class);

    @Autowired
    private IVersionService versionService;

    /**
     * 是否强制升级 0：不强制升级
     */
    private static final Byte IS_PUSH_NO = 0;
    /**
     * 是否强制升级 1：强制升级
     */
    private static final Byte IS_PUSH_YES = 1;

    /**
     * 根据版本序号比对版本
     * 
     * @author Lxl
     * @param versionNo
     * @param response
     * @return
     */
    @RequestMapping("/checkVersion")
    public ResultVo checkVersion(int versionNo, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("isAlert", IS_PUSH_NO);
        List<Version> versions = versionService.findByVersionNo(versionNo);
        int versionNoTemp = versionNo;
        for (Version version : versions) {
            // 版本更新提醒
            if (version.getVersionNo().intValue() > versionNoTemp) {
                result.put("isAlert", IS_PUSH_YES);
                // 版本强制更新
                if (IS_PUSH_YES == version.getIsPush()) {
                    result.put("isPush", IS_PUSH_YES);
                }
                result.put("url", version.getUrl());
                result.put("message", getMessage("version_update"));//
                versionNoTemp = version.getVersionNo().intValue();
            }
        }
        return ResultVo.setResultSuccess(result);
    }

}
