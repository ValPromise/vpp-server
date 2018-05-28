package com.vpp;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vpp.common.utils.ConstantsOrder;
import com.vpp.core.customer.ICustomerService;
import com.vpp.core.deposit.IDepositService;
import com.vpp.core.standardized.order.IOrderService;
import com.vpp.core.telegrambot.ITelegramBotService;
import com.vpp.core.telegrambot.TelegramBotMapper;
import com.vpp.core.vithdrawal.IVithdrawalService;

public class ServiceTest extends TestBase {

    @Autowired
    private ITelegramBotService telegramBotService;

    @Autowired
    private TelegramBotMapper telegramBotMapper;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private IVithdrawalService vithdrawalService;
    @Autowired
    private IOrderService orderService;

    @Test
    public void sendSms() throws Exception {
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("to", "0x8da8A8F2eC5270845730a7C8248211361eb7D869");
        // params.put("amount", "100");
        // String str = SecurityUtils.getSign(params);
        // System.out.println(str);
    }

    @Test
    public void testRisk() throws Exception {
        try {
            String productId = ConstantsOrder.Product.PRODUCT_ID_TEMP;
            Long customerId = 30l;
            String cityId = "CN54511";
            String date = "2018-05-16";
            Integer countByCustomerIdCityId = orderService.fingRiskCountByCustomerIdCityId(productId, customerId, cityId, date);
            System.out.println("-------------" + countByCustomerIdCityId);
            Integer countByCustomerId = orderService.fingRiskCountByCustomerId(productId, customerId, date);
            System.out.println("-------------" + countByCustomerId);
            Integer countByProductId = orderService.fingRiskCountByProductId(productId, cityId, date);
            System.out.println("-------------" + countByProductId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(100).toString());
    }

}
