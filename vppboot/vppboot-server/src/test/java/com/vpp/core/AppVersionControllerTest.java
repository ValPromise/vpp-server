package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.app.version.controller.AppVersionController;

/**
 * 版本管理
 * 
 * @author Lxl
 * @version V1.0 2018年5月30日
 */
public class AppVersionControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(AppVersionControllerTest.class);
    @Autowired
    private AppVersionController appVersionController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String token = "";
    int versionNo = 12;

    @Test
    public void selectContract() throws Exception {
        ResultVo result = appVersionController.checkVersion(versionNo, response);
        logger.info(result);
    }
}
