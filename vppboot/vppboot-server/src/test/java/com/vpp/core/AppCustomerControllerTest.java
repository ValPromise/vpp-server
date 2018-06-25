package com.vpp.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.customer.app.AppCustomerController;

/**
 * 登录测试用例
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
public class AppCustomerControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(AppCustomerControllerTest.class);

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;
    @Autowired
    private AppCustomerController appCustomerController;

    @Autowired
    MockHttpServletResponse response;

    String mobile = "8617322370015";
    String password = "123456";
    String newpassword = "111111";
    String payPassword = "123456";
    String newpayPassword = "123456";
    String token = "29056DA3E424F3BC2E4D79D168F863DA";
    String code = "8348";

    //
    @Test
    public void checkPayPassword() throws Exception {
        // ResultVo res = appCustomerController.isExistPayPassword(token, response);
        // logger.debug("---------------------------");
        // logger.debug(res);
        // logger.debug("-----------");
        //
        // ResultVo res1 = appCustomerController.checkPayPassword(token, payPassword, response);
        // logger.debug("------------------------------");
        // logger.debug(res1);
        // logger.debug("-------------");
        //
//        ResultVo res2 = appCustomerController.updatePayPassword(token, payPassword, newpayPassword, response);
//        logger.debug("------------------------------");
//        logger.debug(res2);
//        logger.debug("-------------");
        //
        // ResultVo res3 = appCustomerController.updatePassword(token, password,newpassword, response);
        // logger.debug("------------------------------");
        // logger.debug(res3);
        // logger.debug("-------------");

        // ResultVo res4 = appCustomerController.updateMobile(token, code,mobile, response);
        // logger.debug("------------------------------");
        // logger.debug(res4);
        // logger.debug("-------------");
    }

//    @Test
//    public void forgetPayPassword() throws Exception {
//        ResultVo res2 = appCustomerController.forgetPayPassword(token, code, newpayPassword, response);
//        logger.debug("------------------------------");
//        logger.debug(res2);
//        logger.debug("-------------");
//    }

    @Test
    public void forgetPassword() throws Exception {
        ResultVo res2 = appCustomerController.forgetPassword(mobile, code, newpassword, response);
        logger.debug("------------------------------");
        logger.debug(res2);
        logger.debug("-------------");
    }
}
