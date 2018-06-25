package com.vpp.core.management.customer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;

/**
 * 管理后台---会员接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月31日
 */
@RestController
@RequestMapping("/mg/customer")
public class MgCustomerController extends CommonController {
    private static final Logger logger = LogManager.getLogger(MgCustomerController.class);
    @Autowired
    private ICustomerService customerService;

    @RequestMapping(value = "/findLimit")
    @ResponseBody
    public ResultVo findLimit(Integer currentPage, Integer pageSize, String mobile, HttpServletResponse response) {
        // response.addHeader("Access-Control-Allow-Origin", "*");
        currentPage = null == currentPage ? 1 : currentPage;
        pageSize = null == pageSize ? 15 : pageSize;

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("mobile", mobile);

            Page<Customer> customers = customerService.findListByCondition(currentPage, pageSize, params);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", customers);
            result.put("total", customers.getTotal());
            result.put("currentPage", customers.getPageNum());
            result.put("pageSize", customers.getPageSize());
            return ResultVo.setResultSuccess(result);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }

    }

}
