package com.vpp.core.management.withdrawal.controller;

import java.util.Date;
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
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.common.EthController;
import com.vpp.core.withdrawal.bean.Withdrawal;
import com.vpp.core.withdrawal.service.IWithdrawalService;

/**
 * 后台提现管理
 * 
 * @author Lxl
 * @version V1.0 2018年6月4日
 */
@RestController
@RequestMapping("/mg/withdrawal")
public class MgWithdrawalController extends CommonController {
    private static final Logger logger = LogManager.getLogger(MgWithdrawalController.class);
    @Autowired
    private IWithdrawalService withdrawalService;
    @Autowired
    private EthController ethController;

    @RequestMapping(value = "/findLimit")
    @ResponseBody
    public ResultVo findLimit(Integer currentPage, Integer pageSize, Long customerId, HttpServletResponse response) {
        // response.addHeader("Access-Control-Allow-Origin", "*");
        currentPage = null == currentPage ? 1 : currentPage;
        pageSize = null == pageSize ? 15 : pageSize;

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("customerId", customerId);

            Page<Withdrawal> tempOrders = withdrawalService.selectWithdrawalList(currentPage, pageSize, params);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", tempOrders);
            result.put("total", tempOrders.getTotal());
            result.put("currentPage", tempOrders.getPageNum());
            result.put("pageSize", tempOrders.getPageSize());
            return ResultVo.setResultSuccess(result);
        } catch (Exception e) {
            return ResultVo.setResultError();
        }

    }

    /**
     * 提现打款-调用以太坊公链RPC 修改提现数据状态为 2：已执行打款
     * 
     * @author Lxl
     * @param id
     * @param response
     * @return
     */
    @RequestMapping(value = "/transfer")
    @ResponseBody
    public ResultVo transfer(Long id, HttpServletResponse response) {
        try {
            Withdrawal wd = withdrawalService.selectByPrimaryKey(id);
            wd.setState(ConstantsServer.WITHDRAWAL_STATE_TRANSFER);// 已执行公链打款
            // WITHDRAWAL_STATE_TRANSFER
            wd.setGmtModified(new Date());
            withdrawalService.updateByPrimaryKeySelective(wd);

            // 提交VPP提现申请至公链，后期需要监控公链事件提现是否确认
            ethController.transfer(wd.getPayeeAddress(), wd.getVpp().doubleValue());
            return ResultVo.setResultSuccess();
        } catch (Exception e) {
            return ResultVo.setResultError();
        }
    }

    /**
     * 提现打款-拒绝提现申请
     * 
     * @author Lxl
     * @param id
     * @param response
     * @return
     */
    @RequestMapping(value = "/reject")
    @ResponseBody
    public ResultVo reject(Long id, HttpServletResponse response) {
        try {
            Withdrawal wd = withdrawalService.selectByPrimaryKey(id);
            wd.setGmtModified(new Date());
            withdrawalService.reject(wd);
            return ResultVo.setResultSuccess();
        } catch (Exception e) {
            return ResultVo.setResultError();
        }
    }

    /**
     * 提现打款-设置提款失败
     * 
     * @author Lxl
     * @param id
     * @param response
     * @return
     */
    @RequestMapping(value = "/fail")
    @ResponseBody
    public ResultVo fail(Long id, HttpServletResponse response) {
        try {
            Withdrawal wd = withdrawalService.selectByPrimaryKey(id);
            wd.setGmtModified(new Date());
            withdrawalService.fail(wd);
            return ResultVo.setResultSuccess();
        } catch (Exception e) {
            return ResultVo.setResultError();
        }
    }
}
