package com.vpp.core.withdrawal.app;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.common.EthController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.withdrawal.bean.Withdrawal;
import com.vpp.core.withdrawal.bean.WithdrawalAccount;
import com.vpp.core.withdrawal.service.IWithdrawalService;

@RestController
@RequestMapping("/app/withdrawal")
public class AppWithdrawalController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppWithdrawalController.class);

    @Autowired
    private IWithdrawalService withdrawalService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private EthController ethController;

    /**
     * 校验钱包地址
     * 
     * @param withdrawalAddress
     * @return
     */
    public boolean checkWithdrawalAddress(String withdrawalAddress) {
        String regexpAddress = "^[A-Za-z0-9]{42}$";
        Pattern pattern = Pattern.compile(regexpAddress);
        Matcher matcher = pattern.matcher(withdrawalAddress);
        return matcher.matches();
    }

    /**
     * 新增提现钱包地址
     * 
     * @param token
     * @param withdrawalAccount
     * @param response
     * @return
     */
    @RequestMapping("/insertWithdrawalAccount")
    public ResultVo insertWithdrawalAccount(String token, WithdrawalAccount withdrawalAccount, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        boolean isMatch = checkWithdrawalAddress(withdrawalAccount.getWithdrawalAddress());
        if (!isMatch) {
            return ResultVo.setResultError("提现钱包地址有误");
        }
        Long customerId = new Long(getCustomerIdByToken(token));
        List<WithdrawalAccount> list = withdrawalService.selectWithdrawalAccountByCustomerId(customerId);
        if (list.size() >= 10) {
            return ResultVo.setResultError("最多可添加10个提现钱包地址");
        }
        withdrawalAccount.setGmtCreate(new Date());
        withdrawalAccount.setCustomerId(customerId);
        int ret = withdrawalService.insertWithdrawalAccount(withdrawalAccount);
        return ret > 0 ? ResultVo.setResultSuccess(getMessage("add_withdrawal_account_success"))
                : ResultVo.setResultError(getMessage("add_withdrawal_account_fail"));
    }

    /**
     * 查询提现钱包地址列表
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/withdrawalAccountList")
    public ResultVo withdrawalAccountList(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        List<WithdrawalAccount> list = withdrawalService
                .selectWithdrawalAccountByCustomerId(new Long(getCustomerIdByToken(token)));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 修改提现钱包地址
     * 
     * @param token
     * @param withdrawalAccount
     * @param response
     * @return
     */
    @RequestMapping("/updateWithdrawalAccount")
    public ResultVo updateWithdrawalAccount(String token, WithdrawalAccount withdrawalAccount, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        boolean isMatch = checkWithdrawalAddress(withdrawalAccount.getWithdrawalAddress());
        if (!isMatch) {
            return ResultVo.setResultError("提现钱包地址有误");
        }
        withdrawalAccount.setGmtModified(new Date());
        int ret = withdrawalService.updateWithdrawalAccount(withdrawalAccount);
        return ret > 0 ? ResultVo.setResultSuccess("修改钱包地址成功") : ResultVo.setResultError("修改钱包地址失败");
    }

    /**
     * 提现记录查询
     * 
     * @param token
     * @param pageNum
     * @param pageSize
     * @param response
     * @return
     */
    @RequestMapping("/withdrawalList")
    public ResultVo selectWithdrawalList(String token, Integer pageNum, Integer pageSize, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 15;
        }
        String customerId = getCustomerIdByToken(token);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", customerId);
        Page<Withdrawal> list = withdrawalService.selectWithdrawalList(pageNum, pageSize, map);
        Map<String, Object> resutMap = new HashMap<String, Object>();
        resutMap.put("currentPage", list.getPageNum());
        resutMap.put("pageSize", list.getPageSize());
        resutMap.put("total", list.getTotal());
        resutMap.put("rows", list);

        return ResultVo.setResultSuccess(resutMap);
    }

    /**
     * 发起提现
     * 
     * @author Lxl
     * @param token
     * @param withdrawal
     * @param response
     * @return
     */
    @RequestMapping("/withdrawal")
    public ResultVo withdrawal(String token, Withdrawal withdrawal, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        if (null == withdrawal.getVpp() || StringUtils.isBlank(withdrawal.getPayeeAddress())) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        // 收款地址
        if (StringUtils.isBlank(withdrawal.getPayeeAddress())) {
            return ResultVo.setResultError();
        }

        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }
        BigDecimal blance = customer.getBalance();
        if (DealUtil.priceCompare(withdrawal.getVpp(), blance, ">")) {
            return ResultVo.setResultSuccess("提现金额大于可用余额");
        }

        // 判断最小提现金额
        if (withdrawal.getVpp().doubleValue() < ConstantsServer.WITHDRAWAL_AMOUNT_MIN) {
            return ResultVo.setResultError(getMessage("parameter_error"));
        }

        withdrawal.setCustomerId(new Long(customerId));
        withdrawal.setGmtCreate(new Date());
        withdrawal.setState(ConstantsServer.WITHDRAWAL_STATE_WAIT);
        try {
            // 提交VPP提现申请至公链，后期需要监控公链事件提现是否确认，管理后台手动打款
            withdrawalService.withdrawal(withdrawal);
        } catch (Exception e) {
            logger.error("withdrawal error ::: {}", e.getMessage());
            ResultVo.setResultError("提现失败");
        }
        return ResultVo.setResultSuccess("申请提交成功");
    }

}
