package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.vpp.Application;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.customer.app.AppLoginController;
import com.vpp.core.customer.bean.Customer;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AppLoginTest {

    private static final Logger logger = LogManager.getLogger(AppLoginTest.class);
    @Autowired
    private AppLoginController appLoginController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String mobile = "8617322370006";
    String password = "123456";

    @Test
    public void getMobileCode() throws Exception {
        ResultVo res = appLoginController.getMobileCode(mobile, response);

        logger.debug(res);
    }

    @Test
    public void register() throws Exception {
        Customer customer = new Customer();
        customer.setMobile(mobile);
        customer.setPassword(password);
        customer.setInviteCode("525z");
        String code = "5234";
        ResultVo res = appLoginController.register(customer, code, response);
        logger.debug(res);
    }
    
    @Test
    public void login() throws Exception {
        ResultVo res = appLoginController.login(mobile, password, response);
        
        logger.debug("-----");
        logger.debug(res);
        logger.debug("-----");
    }

    @Test
    public void loginCode() throws Exception {
        String code = "3252";
        ResultVo res = appLoginController.loginCode(mobile, code, response);

        logger.debug("-----");
        logger.debug(res);
        logger.debug("-----");
    }

}
