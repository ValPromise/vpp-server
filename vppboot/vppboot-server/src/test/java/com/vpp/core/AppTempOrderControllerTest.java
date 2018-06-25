package com.vpp.core;

import java.util.List;
import java.util.Map;

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
import com.vpp.core.app.temp.controller.AppTempContractController;
import com.vpp.core.app.temp.controller.AppTempOrderController;

/**
 * 降雨合约测试用例
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AppTempOrderControllerTest {

    private static final Logger logger = LogManager.getLogger(AppTempOrderControllerTest.class);
    @Autowired
    private AppTempOrderController appOrderController;
    @Autowired
    private AppTempContractController appTempContractController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String mobile = "8617322370015";
    String password = "8617322370015";
    String token = "856E0928805278497422045ECAAF2C8E";
    Integer currentPage = 10;
    Integer pageSize = 20;

    @Test
    public void getOrderList() throws Exception {
        ResultVo res = appOrderController.getOrderList(token, currentPage, pageSize, response);
        logger.debug("-----");
        logger.debug(res.getData());
        logger.debug("-----");
    }

    @Test
    public void getOrderInfo() throws Exception {
        String innerOrderId = "V652973454967";
        ResultVo res = appOrderController.getOrderInfo(token, innerOrderId, response);
        logger.debug("-----");
        logger.debug(res.getData());
        logger.debug("-----");
    }

    @Test
    public void addOrder() throws Exception {
        String cityId = "CN54511";

        ResultVo contract = appTempContractController.getContract(token, cityId, response);
        Map<String, Object> contractMap = (Map<String, Object>) contract.getData();
        String contractId = (String) contractMap.get("contractId");

        List<Map<String, Object>> contractList = (List<Map<String, Object>>) contractMap.get("contractList");
        Map<String, Object> ct = contractList.get(0);
        Float strike = Float.valueOf(ct.get("strike").toString());
        int buyCnt = 3;

        ResultVo res = appOrderController.addOrder(token, contractId, strike, buyCnt, response);
        logger.debug("-----");
        logger.debug(res);
        logger.debug("-----");
    }
}
