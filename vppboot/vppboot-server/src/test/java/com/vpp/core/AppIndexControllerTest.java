package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.app.index.controller.AppIndexController;

public class AppIndexControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(AppIndexControllerTest.class);
    @Autowired
    private AppIndexController appIndexController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String mobile = "8617322370015";

    @Test
    public void getMobileCode() throws Exception {
        ResultVo res = appIndexController.index(mobile, response);
        logger.debug(res);
    }

}
