package com.vpp.core.withdrawal;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.DealUtil;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.Customer;
import com.vpp.core.customer.ICustomerService;
import com.vpp.vo.ResultVo;

@RequestMapping("/withdrawal")
@RestController
public class WithdrawalController extends CommonController {
	
	private static final Logger logger = LogManager.getLogger(WithdrawalController.class);
	
	@Autowired
	private IWithdrawalService withdrawalService;
	@Autowired
	private ICustomerService customerService;
	
	@RequestMapping("/withdrawalEth")
	public ResultVo withdrawalEth(Withdrawal withdrawal,String token,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(!checkLogin(token)){
			return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		}
	    Customer customer = customerService.selectCustomerById(new Long(getTokenId(token)));
		if(customer==null){
			return ResultVo.setResultError("提现失败");
		}
		if(DealUtil.priceCompare(withdrawal.getAmount(), customer.getBalance(), ">")){
			return ResultVo.setResultError("余额不足");
		}
	    withdrawal.setGmtCreate(new Date());
		withdrawal.setCustomerId(customer.getId());
		withdrawal.setFromAccount(customer.getDepositAccount());
		withdrawal.setToAccount(customer.getWithdrawalAccount());
		int ret = withdrawalService.withdrawalEth(withdrawal);
		return ret>0?ResultVo.setResultSuccess("提现成功"):ResultVo.setResultError("提现失败");
	}
	
	

}
