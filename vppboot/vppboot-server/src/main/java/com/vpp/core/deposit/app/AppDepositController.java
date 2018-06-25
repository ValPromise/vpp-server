package com.vpp.core.deposit.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.deposit.service.IDepositService;

/**
 * 充值接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月30日
 */
@RestController
@RequestMapping("/app/deposit")
public class AppDepositController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppDepositController.class);

    @Autowired
    private IDepositService depositService;

    /**
     * VPP充值记录查询
     * 
     * @param pageNum
     * @param pageSize
     * @param response
     * @return
     */
    @RequestMapping("/selectDepositInfo")
    public ResultVo selectDepositInfo(Integer pageNum, Integer pageSize, String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
//        if (!checkLogin(token)) {
//            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
//        }
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 15;
        }
        String customerId = getCustomerIdByToken(token);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", customerId);
        Page<Map<String, Object>> list = depositService.selectDepositInfo(pageNum, pageSize, map);
        Map<String, Object> resutMap = new HashMap<String, Object>();
        resutMap.put("currentPage", list.getPageNum());
        resutMap.put("pageSize", list.getPageSize());
        resutMap.put("total", list.getTotal());
        resutMap.put("rows", list);
        return ResultVo.setResultSuccess(resutMap);
    }

    /***
     * 账户-充值记录入库
     * 
     * @param account
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/syncDepositByAccount")
    public ResultVo syncDepositByAccount(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
//        if (!checkLogin(token)) {
//            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
//        }
        Customer customer = this.findCustomerByToken(token);
        // 同步当前用户的充值记录
        depositService.syncDepositByAccount(customer.getDepositAddress());
        return ResultVo.setResultSuccess();
    }

}
