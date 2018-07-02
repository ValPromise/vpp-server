package com.vpp.core.management.index.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.coinguess.service.ICoinguessService;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.service.ICustomerService;

/**
 * 首页接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月31日
 */
@RestController
@RequestMapping("/mg/index")
public class MgIndexController extends CommonController {
    private static final Logger logger = LogManager.getLogger(MgIndexController.class);
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICoinguessService coinguessService;

    /**
     * 统计数据
     * 
     * @author Lxl
     * @return
     */
    @RequestMapping(value = "/stata")
    @ResponseBody
    public ResultVo stata(String startDate) {
        try {
            if (StringUtils.isBlank(startDate)) {
                startDate = DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN);
            }
            // String startGmtCreate = "2016-05-05";
            // 当天注册用户数
            Integer customerTodayCount = customerService.findCount(startDate, null);
            // 累计注册用户数
            Integer customerTotalCount = customerService.findCount(null, null);
            // 各个合约当天下单量,下单金额
            List<Map<String, Object>> coinTodayCount = coinguessService.findCount(startDate, null);

            // 各个合约总下单量,下单金额
            List<Map<String, Object>> coinTotalCount = coinguessService.findCount(null, null);

            // 当天赔付金额
            List<Map<String, Object>> coinTodayPayout = coinguessService.findPayout(startDate, null);

            // 各个合约总赔付金额
            List<Map<String, Object>> coinTotalPayout = coinguessService.findPayout(null, null);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("customerTodayCount", customerTodayCount);
            result.put("customerTotalCount", customerTotalCount);
            result.put("coinTodayCount", coinTodayCount);
            result.put("coinTotalCount", coinTotalCount);
            result.put("coinTodayPayout", coinTodayPayout);
            result.put("coinTotalPayout", coinTotalPayout);
            return ResultVo.setResultSuccess(result);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }

    }

}
