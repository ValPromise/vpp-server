package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.core.deposit.service.IDepositService;

/**
 * 充值接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月30日
 */
public class DepositServiceTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(DepositServiceTest.class);
    @Autowired
    private IDepositService depositService;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String account = "0x13328a9cd26a5d7cc51b6521ce76b2d31fe2e280";

    @Test
    public void selectContract() throws Exception {

        depositService.syncDepositByAccount(account);

    }
}
