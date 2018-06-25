package com.vpp.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
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
import com.vpp.core.app.city.AppCityController;
import com.vpp.service.city.bean.CityInfo;
import com.vpp.service.city.bean.CityVo;
import com.vpp.service.city.service.ICityService;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CityTest {
    private static final Logger logger = LogManager.getLogger(CityTest.class);
    @Autowired
    private AppCityController appCityController;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpServletResponse response;

    @Autowired
    MockHttpSession session;

    @Autowired
    private ICityService cityService;

    @Test
    public void triggerByEtime() throws Exception {
        String cityId = "CN54511";
        String cnName = "北京";
        CityInfo cityInfoFirst = cityService.findCityInfoByCityId(cityId);
        logger.debug(ToStringBuilder.reflectionToString(cityInfoFirst));
        CityInfo cityInfoSecond = cityService.findCityInfoByCnName(cnName);
        logger.debug(ToStringBuilder.reflectionToString(cityInfoSecond));

        List<String> cityIds = new ArrayList<String>();
        cityIds.add("CN50850");
        cityIds.add("CN54511");
        cityIds.add("CN59493");

        List<CityVo> citys = cityService.findListByCityIds(cityIds);
        logger.debug(citys);

        ResultVo result = appCityController.tempList(response);
        logger.debug(result);
    }

}
