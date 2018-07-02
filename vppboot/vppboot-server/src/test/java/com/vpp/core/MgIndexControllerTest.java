package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.management.index.controller.MgIndexController;

/**
 * 后台统计
 * 
 * @author Lxl
 * @version V1.0 2018年6月26日
 */
public class MgIndexControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(MgIndexControllerTest.class);

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;
    @Autowired
    private MgIndexController mgIndexController;

    @Autowired
    MockHttpServletResponse response;

    //
    @Test
    public void checkPayPassword() throws Exception {
        ResultVo res4 = mgIndexController.stata("2016-05-05");
        logger.debug("------------------------------");
        logger.debug(res4);
        logger.debug("-------------");
    }
}
