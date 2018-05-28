package com.vpp.core.customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.vpp.common.filter.SendSmsUtil;
import com.vpp.common.utils.StringUtils;
import com.vpp.core.common.CommonController;
import com.vpp.core.common.EthController;
import com.vpp.core.deposit.DepositController;
import com.vpp.core.deposit.IDepositService;
import com.vpp.vo.ResultVo;

@RestController
@RequestMapping("/customer")
public class CustomerController extends CommonController {
 	 private static final Logger logger = LogManager.getLogger(CustomerController.class);
	 @Autowired
	 private ICustomerService customerService;
	 @Autowired
	 private DepositController depositController;
	 @Autowired
	 private IDepositService depositService;
	 @Autowired
	 private Gson gson;
	 /**
	  * valbank
	  * 获取手机验证码
	  * @param response
	  * @return
	  */
	 @RequestMapping("/getMobileCode")
	 public ResultVo getCode(String mobile,HttpServletResponse response){
		 response.addHeader("Access-Control-Allow-Origin", "*");
		 String code = SendSmsUtil.getCaptcha(); 
		 String result = SendSmsUtil.sendSmsChuangLan(mobile,code);
		 if(result.equals("fail")){  //短信发送失败
			 return ResultVo.setResultError(getMessage("send_sms_fail"));
		 }
		 setMobileCodeRedis(mobile,code); 
		 logger.info("验证码："+mobile+"\t"+code);
		 return ResultVo.setResultSuccess();
	 }
	 
	/**
	 * 注册 valbank
	 * @param mobile
	 * @param code
	 * @param response
	 * @return
	 */
	@RequestMapping("/register")
	public ResultVo register(Customer customer,String mobileCode,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		String mobile = customer.getMobile();
		if(!SendSmsUtil.isMobile(mobile)){
			return ResultVo.setResultError(getMessage("mobile_format_error"));
		}
		if(!checkMobileCode(mobile,mobileCode)){
			return ResultVo.setResultError(getMessage("mobile_check_error"));
		}
	    Customer ct = customerService.selectCustomerByUserName(mobile);
	    if(ct!=null){
	    	if(mobile.equals(ct.getMobile())){  //手机号已被注册
	 	    	return ResultVo.setResultError(getMessage("mobile_used"));
	 	    }
	    } 
	    String account = EthController.createAccount(); //生成充值账号
	    if(StringUtils.isEmpty(account)){
	    	 return ResultVo.setResultError(getMessage("create_account_fail"));
	    }
	    if(customerService.getCustomerByAccount(account)!=null){  //重复校验
	    	 return ResultVo.setResultError(getMessage("create_account_fail"));
	    }
	    customer.setDepositAccount(account);  
	    customer.setGmtCreate(new Date());
	    Float dayRate = 0.00013F;
	    Float yearRate = dayRate*365;
	    customer.setRate(yearRate);
	    int ret = customerService.insertCustomer(customer);
	    if(ret>0){
	    	String token = setLoginToken(customer.getId().toString());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token",token);
			return ResultVo.setResultSuccess(getMessage("register_success"),map);
	    }
	    return ResultVo.setResultError(getMessage("register_error"));
	}
 	 
 
     	/**
		 * 登录
		 * @param mobile 手机号码
		 * @param code 手机验证码
		 * @param imei 手机串码
		 * @param response
		 * @return
		 */
		@RequestMapping("/login")
		public ResultVo login(String mobile,String mobileCode,HttpServletResponse response){
			response.addHeader("Access-Control-Allow-Origin", "*");
			if(!SendSmsUtil.isMobile(mobile)){
				return ResultVo.setResultError(getMessage("mobile_format_error"));
			}
			if(!checkMobileCode(mobile,mobileCode)){
				return ResultVo.setResultError(getMessage("mobile_check_error"));
			}
		    Customer customer = customerService.selectCustomerByUserName(mobile);
		    if(customer==null){  //账号不存在，请注册
		    	return ResultVo.setResultError(getMessage("account_no"));
		    }
		    String token = setLoginToken(customer.getId().toString()); //登录缓存token
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token",token); 
			ResultVo  resultVo =  ResultVo.setResultSuccess(map); 
			return resultVo;
		}
	
		/**
		 * 退出
		 * @param token
		 * @param response
		 * @return
		 */
		@RequestMapping("/loginOut")
		public ResultVo loginOut(String token,HttpServletResponse response){
			response.addHeader("Access-Control-Allow-Origin", "*");
			if(!checkLogin(token)){
				 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
			}
		    boolean bool = delLoginToken(token);
		    if(!bool){
		    	 return ResultVo.setResultError("fail");
		    }
			return ResultVo.setResultSuccess("success");
		}
	 
	 
	 /**
	  * 账户详情
	  * @param customer
	  * @param response
	  * @param request
	  * @return
	  */
	 @RequestMapping("/customerDetail")
	 public ResultVo customerDetail(String token,HttpServletResponse response){
		 response.addHeader("Access-Control-Allow-Origin", "*");
		 if(!checkLogin(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		 }
		 String customerId = getTokenId(token);
		 Customer customer = customerService.selectCustomerById(new Long(customerId));
		 if(customer==null){
			 return ResultVo.setResultError();
		 }
		 String depositAccount = customer.getDepositAccount();
		 depositController.vppPay(depositAccount,response);  //查询充值记录入库
		 customer = customerService.selectCustomerById(new Long(customerId));
		 BigDecimal value = depositService.depositListById(new Long(customerId),customer.getRate());
		 BigDecimal balance = customer.getBalance();
		 Double eth = customerService.getEthOpenValue()*customerService.getFromUsdToCny();  
		 Double rmb =  balance.multiply(new BigDecimal(eth)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		 Map<String,Object> map = new HashMap<String, Object>();  
		 map.put("id", customerId);
		 map.put("balance",balance.add(value)); 
		 map.put("cny", rmb);
		 map.put("name", customer.getName());
		 map.put("mobile", customer.getMobile()); 
		 return  ResultVo.setResultSuccess(map);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
	 }
  
	 /**
	  * 获取内部钱包地址
	  * @param token
	  * @param response
	  * @return
	  */
	 @RequestMapping("/getDepositAccount")
	 public ResultVo getDepositAccount(String token,HttpServletResponse response){
		 response.addHeader("Access-Control-Allow-Origin", "*");
		 if(!checkLogin(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		 }
		 String customerId = getTokenId(token);
		 Customer customer = customerService.selectCustomerById(new Long(customerId));
		 String url ="http://qr.liantu.com/api.php";
		 String depositAccount = customer.getDepositAccount();
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("depositAccountCode",url+"?text="+depositAccount);
		 map.put("depositAccount", depositAccount);
		 return  ResultVo.setResultSuccess(map);
	 }
	 
	 /**
	  * 获取手机号码
	  * @param token
	  * @param response
	  * @return
	  */
	 @RequestMapping("/getMobile")
	 public ResultVo getMobile(String token,HttpServletResponse response){
		 response.addHeader("Access-Control-Allow-Origin", "*");
		 if(!checkLogin(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		 }
		 String customerId = getTokenId(token);
		 Customer customer = customerService.selectCustomerById(new Long(customerId));
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("mobile",customer.getMobile());
		 return  ResultVo.setResultSuccess(map);
	 }
 
}
