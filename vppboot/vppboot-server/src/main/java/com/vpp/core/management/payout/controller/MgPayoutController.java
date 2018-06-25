package com.vpp.core.management.payout.controller;

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
import com.vpp.core.standardized.payout.IOrderPayoutService;
import com.vpp.core.standardized.payout.OrderPayout;

/**
 * 管理后台-订单赔付记录
 * 
 * @author Lxl
 * @version V1.0 2018年6月5日
 */
@RestController
@RequestMapping("/mg/payout")
public class MgPayoutController extends CommonController {
    private static final Logger logger = LogManager.getLogger(MgPayoutController.class);

    @Autowired
    private IOrderPayoutService orderPayoutService;

    /**
     * 分页查询
     * 
     * @author Lxl
     * @param currentPage
     * @param pageSize
     * @param response
     * @return
     */
    @RequestMapping("/findLimit")
    public ResultVo findLimit(Integer currentPage, Integer pageSize, HttpServletResponse response) {
        // response.addHeader("Access-Control-Allow-Origin", "*");
        currentPage = null == currentPage ? 1 : currentPage;
        pageSize = null == pageSize ? 15 : pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        Page<OrderPayout> list = orderPayoutService.findLimit(currentPage, pageSize, map);
        Map<String, Object> resutMap = new HashMap<String, Object>();
        resutMap.put("currentPage", list.getPageNum());
        resutMap.put("pageSize", list.getPageSize());
        resutMap.put("total", list.getTotal());
        resutMap.put("rows", list);
        return ResultVo.setResultSuccess(resutMap);
    }

}
