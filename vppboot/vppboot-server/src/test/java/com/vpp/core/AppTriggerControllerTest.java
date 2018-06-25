package com.vpp.core;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.utils.HttpUtils;
import com.vpp.core.app.trigger.controller.AppTriggerController;

import net.sf.json.JSONObject;

/**
 * 触发
 * 
 * @author Lxl
 * @version V1.0 2018年6月6日
 */
public class AppTriggerControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(AppTriggerControllerTest.class);
    @Autowired
    private AppTriggerController appTriggerController;

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
    public void rain() throws Exception {
//        appTriggerController.rain(token, etime, response);
    }

    @Test
    public void temp() throws Exception {
        appTriggerController.temp(token, etime, response);
    }
    
    public static void main(String[] args){
        String data = HttpUtils.get("http://47.75.91.177:8001/", new HashMap<String, String>());
        JSONObject huoEthUsdt = JSONObject.fromObject(data);
        System.out.println(huoEthUsdt.getDouble("price"));
    }
}
