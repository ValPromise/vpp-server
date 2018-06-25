package com.vpp.core;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.core.withdrawal.service.IWithdrawalService;

/**
 * 充值接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月30日
 */
public class WithdrawalServiceTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(WithdrawalServiceTest.class);
    @Autowired
    private IWithdrawalService withdrawalService;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String account = "0x13328a9cd26a5d7cc51b6521ce76b2d31fe2e280";

    @Test
    public void syncWithdrawalData() throws Exception {

        withdrawalService.syncWithdrawalData();

    }
}
