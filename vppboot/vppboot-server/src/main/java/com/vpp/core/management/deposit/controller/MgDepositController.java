package com.vpp.core.management.deposit.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.deposit.bean.Deposit;
import com.vpp.core.deposit.service.IDepositService;

/**
 * 管理后台-充值接口
 * 
 * @author Lxl
 * @version V1.0 2018年6月5日
 */
@RestController
@RequestMapping("/mg/deposit")
public class MgDepositController extends CommonController {
    private static final Logger logger = LogManager.getLogger(MgDepositController.class);

    @Autowired
    private IDepositService depositService;

    @Autowired
    private ICustomerService customerService;

    /**
     * 充值记录--分页查询
     * 
     * @author Lxl
     * @param currentPage
     * @param pageSize
     * @param response
     * @return
     */
    @RequestMapping("/findLimit")
    public ResultVo findLimit(Integer currentPage, Integer pageSize, HttpServletResponse response) {
        currentPage = null == currentPage ? 1 : currentPage;
        pageSize = null == pageSize ? 15 : pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        Page<Deposit> list = depositService.findLimit(currentPage, pageSize, map);
        Map<String, Object> resutMap = new HashMap<String, Object>();
        resutMap.put("currentPage", list.getPageNum());
        resutMap.put("pageSize", list.getPageSize());
        resutMap.put("total", list.getTotal());
        resutMap.put("rows", list);
        return ResultVo.setResultSuccess(resutMap);
    }

    /**
     * 后台手动充值，空投
     * 
     * @author Lxl
     * @param mobile
     * @param amount
     * @param response
     * @return
     */
    @RequestMapping("/add")
    public ResultVo add(String mobile, Integer amount, HttpServletResponse response) {
        try {
            Customer customer = customerService.findByMobile(mobile);
            if (null != customer) {
                customerService.incomeBalance(customer.getId(), new BigDecimal(amount), "后台手动充值");
                return ResultVo.setResultSuccess();
            } else {
                return ResultVo.setResultError("customer is null");
            }
        } catch (Exception e) {
            return ResultVo.setResultError(e.getMessage());
        }
    }
}
