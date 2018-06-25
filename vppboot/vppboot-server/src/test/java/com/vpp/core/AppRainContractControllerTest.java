package com.vpp.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.app.rain.controller.AppRainContractController;
import com.vpp.core.app.rain.controller.AppRainOrderController;

/**
 * 降雨合约测试用例
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
public class AppRainContractControllerTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(AppRainContractControllerTest.class);
    @Autowired
    private AppRainContractController appRainContractController;
    @Autowired
    private AppRainOrderController appRainOrderController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String token = "";
    String cityId = "CN59493";

    @Test
    public void addOrder() throws Exception {
        for (int i = 0; i < 5; i++) {
            ResultVo res = appRainContractController.selectContract(token, cityId, response);
            logger.debug("-----");
            logger.debug(res);
            logger.debug("-----");
            Integer buyCnt = 3;
            // Map<String, Object> result = new HashMap<String, Object>();
            // result.put("explainFirst", explainFirst);
            // result.put("explainSecond", explainSecond);
            // result.put("contractTime", dataModelDto.getStime());
            // result.put("contractId", dataModelDto.getInnerOrderId());
            // result.put("payout", dataModelDto.getPayoutFee());
            // result.put("price", dataModelDto.getPrice());
            // result.put("threshold", dataModelDto.getThreshold());
            Map<String, Object> result = (Map<String, Object>) res.getData();
            String contractId = result.get("contractId").toString();
            Float threshold = (Float) result.get("threshold");

            appRainOrderController.addOrder(token, contractId, threshold, buyCnt, response);
        }
    }
    
    @Test
    public void selectContract() throws Exception {
        ResultVo res = appRainContractController.selectContract(token, cityId, response);
        logger.debug("-----");
        logger.debug(res);
        logger.debug("-----");
    }

    @Test
    public void getOrderInfo() throws Exception {
        ResultVo res = appRainOrderController.getOrderInfo(token, "R442863638138", response);
        logger.debug("-----");
        logger.debug(res);
        logger.debug("-----");
    }
}
