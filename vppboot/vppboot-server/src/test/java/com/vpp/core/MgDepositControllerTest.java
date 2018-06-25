package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.core.management.deposit.controller.MgDepositController;

public class MgDepositControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(MgDepositControllerTest.class);
    @Autowired
    private MgDepositController mgDepositController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String token = "";
    String cityId = "CN59493";
    String etime = "2018-06-02";

    @Test
    public void add() throws Exception {
        String mobile = "8617322370015";
        Integer amount = 11;
        mgDepositController.add(mobile, amount, response);
    }
}
