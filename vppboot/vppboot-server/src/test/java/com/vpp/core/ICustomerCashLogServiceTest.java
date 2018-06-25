package com.vpp.core;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.management.cashlog.controller.MgCustomerCashLogController;

/**
 * 资金流水
 * 
 * @author Lxl
 * @version V1.0 2018年6月6日
 */
public class ICustomerCashLogServiceTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(ICustomerCashLogServiceTest.class);
    @Autowired
    private ICustomerCashLogService customerCashLogService;
    @Autowired
    private MgCustomerCashLogController customerCashLogController;
    @Autowired
    private ICustomerService customerService;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletResponse response;

    String token = "";
    String desc = "test";
    Long customerId = 10l;
    BigDecimal amount = new BigDecimal(200);

    @Test
    public void expenditure() throws Exception {
        // customerCashLogService.insertDeposit(customerId, amount, "充值");
        // customerCashLogService.insertWithdrawal(customerId, amount, "提现");
        // customerCashLogService.insertPay(customerId, amount, "购买支付");
        // customerCashLogService.insertPayout(customerId, amount, "赔付收款");
        // 201813
        amount = new BigDecimal(13);
        // customerCashLogService.expenditure(customerId, amount, desc);

    }

    @Test
    public void expenditureBalance() throws Exception {
        amount = new BigDecimal(11);
        // customerService.expenditureBalance(customerId, amount, "支出");
    }

    @Test
    public void incomeBalance() throws Exception {
        amount = new BigDecimal(5);
        // customerService.incomeBalance(customerId, amount, "收入");
    }

    @Test
    public void findLimitByCondition() throws Exception {
        Integer currentPage = 1;
        Integer pageSize = 10;
        ResultVo resultVo = customerCashLogController.findLimit(currentPage, pageSize, "", response);
        System.out.println(resultVo);
    }
}
