//package com.vpp.core;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.vpp.Application;
//import com.vpp.common.vo.ResultVo;
//import com.vpp.core.coinguess.app.CoinguessController;
//import com.vpp.service.city.bean.CityInfo;
//import com.vpp.service.city.bean.CityVo;
//import com.vpp.core.coinguess.service.ICoinguessService;
//
//@SpringBootTest(classes = Application.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//public class CoinguessControllerTest {
//    private static final Logger logger = LogManager.getLogger(CoinguessControllerTest.class);
//    @Autowired
//    private CoinguessController coinguessController;
//
//    @Autowired
//    MockHttpServletRequest request;
//
//    @Autowired
//    MockHttpServletResponse response;
//
//    @Autowired
//    MockHttpSession session;
//
//    @Autowired
//    private ICoinguessService coinguessService;
//
//    @Test
//    public void triggerByEtime() throws Exception {
//
//    }
//
//}
