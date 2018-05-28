package com.vpp.core.deposit;

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
import com.vpp.core.common.CommonController;
import com.vpp.vo.ResultVo;

@RestController
@RequestMapping("/deposit")
public class DepositController extends CommonController{
	private static final Logger logger = LogManager.getLogger(DepositController.class);

	@Autowired
	private IDepositService depositService;
	
	/**
	 * vpp充值  -->不用
	 * @param deposit
	 * @return
	 */
	@RequestMapping("/insertDeposit")
	public ResultVo insertDeposit(Deposit deposit,String token,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		 if(StringUtils.isEmpty(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		 }
		 if(!checkLogin(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		 }
		deposit.setCustomerId(new Long(getTokenId(token)));
		int ret = depositService.insertDeposit(deposit);
		return ret>0?ResultVo.setResultSuccess("充值成功"):ResultVo.setResultError("充值失败");
	}
	
	/**
	 * VPP充值记录查询
	 * @param pageNum
	 * @param pageSize
	 * @param response
	 * @return
	 */
	@RequestMapping("/depositList")
	public ResultVo selectDepositInfo(Integer pageNum,Integer pageSize,String token,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(!checkLogin(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		}
		if(StringUtils.isEmpty(pageNum)){
			pageNum = 1;
		}
		if(StringUtils.isEmpty(pageSize)){
			pageSize = 15;
		}
		String customerId = getTokenId(token);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
	    Page<Deposit> list =depositService.selectDepositInfo(pageNum, pageSize, map);
		Map<String, Object> resutMap = new HashMap<String, Object>();
		resutMap.put("currentPage", list.getPageNum());
		resutMap.put("pageSize", list.getPageSize());
		resutMap.put("total", list.getTotal());
		resutMap.put("rows",list);
	    return ResultVo.setResultSuccess(resutMap);
	}
	
    /***
     * 账户-充值记录入库
     * @param account
     * @param token
     * @param response
     * @return
     */
	@RequestMapping("/vppPay")
	public void vppPay(String account,HttpServletResponse response){
		 response.addHeader("Access-Control-Allow-Origin", "*");
		 Long fromBlock = depositService.getMaxBlockNumber(account);  //获取最大区块号
		 if(fromBlock==null){
			 fromBlock = 0L;
		 }
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("account", account);
		 params.put("fromBlock", fromBlock.toString());
		// depositService.vppPay(params);
	}
	
}
