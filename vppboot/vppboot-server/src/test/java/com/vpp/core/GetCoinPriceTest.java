package com.vpp.core;

import com.vpp.Application;
import com.vpp.core.coinguess.service.ICoinguessService;
import com.vpp.core.rain.service.IRainOrderTriggerService;
import com.vpp.core.standardized.payout.IOrderPayoutService;
import com.vpp.core.standardized.trigger.ITriggerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class GetCoinPriceTest {

//    @Autowired
//    private ICoinguessService coinguessService;
//
//    @Test
//    public void triggerByEtime() throws Exception {
//        coinguessService.runTheLottery();
//    }


}
