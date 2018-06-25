package com.vpp.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.vpp.Application;
import com.vpp.core.rain.service.IRainOrderTriggerService;
import com.vpp.core.standardized.payout.IOrderPayoutService;
import com.vpp.core.standardized.trigger.ITriggerService;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TriggerTest {

    @Autowired
    private ITriggerService triggerService;
    @Autowired
    private IOrderPayoutService orderPayoutService;
    @Autowired
    private IRainOrderTriggerService rainOrderTriggerService;

    @Test
    public void triggerByEtime() throws Exception {
        String etime = "2018-05-31";
        // triggerService.triggerByEtime(etime);
        
        rainOrderTriggerService.triggerByEtime(etime);
    }

    @Test
    public void payout() throws Exception {
//        orderPayoutService.payoutRemitToUser();
    }
}
