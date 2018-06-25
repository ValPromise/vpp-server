package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.core.common.EthController;

/**
 * 降雨合约测试用例
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
public class EthTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(EthTest.class);
    @Autowired
    private EthController ethController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String token = "";
    String cityId = "CN59493";
    String to = "0x13328a9cd26a5d7cc51b6521ce76b2d31fe2e280";
    Double amount = 2000.0d;

    @Test
    public void addOrder() throws Exception {
        ethController.transfer(to, amount);
    }
}
