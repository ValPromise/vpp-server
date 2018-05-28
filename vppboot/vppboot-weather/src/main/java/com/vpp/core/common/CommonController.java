package com.vpp.core.common;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;

import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.StringUtils;

public class CommonController {

	private static final Logger logger = LogManager.getLogger(CommonController.class);

	public static final int CACHE_THIRTY_MINUTE = 60 * 30;// 30分钟

	public static final String VPP_LOGIN_KEY = "vpp_login_key_";

	public static final String LOGIN_VPP_MOBILE_CODE = "login_vpp_mobile_code_";

	public static final int MOBILE_CODE_CACHE_THIRTY_MINUTE = 60*5;// 5分钟
	
	public static final String TOKEN_FAIL_ERROR_CODE = "1001";
	
	public static final String VPP_IMEI_KEY ="vpp_imei_key_";
	

	@Autowired
	private RedisTemplate<String, String> loginTokenRedis;
	@Autowired
	private RedisTemplate<String, String> mobileCodeTokenRedis;
	
	@Autowired
	private RedisTemplate<String, String> iemiTokenRedis;

	@Autowired
	private MessageSource messageSource;
	
	
	/**
	 * 登录缓存token
	 * @param id
	 * @return
	 */
	public String setLoginToken(String id){
		  String dateStr = DateUtil.format(new Date(),DateUtil.LONG_DATE_TIME_PATTERN_SSS);
		  String token = MD5Utils.getMD5String(id+dateStr);
		  String setToken = VPP_LOGIN_KEY+token;
		  try {
			  loginTokenRedis.opsForValue().set(setToken,id);
		  } catch (Exception e) {
			  logger.error("发生异常setLoginToken():"+e.getMessage());
		  }
		  return token;
	}
	 
	/**
	 * 校验登录状态
	 * @param token
	 * @return
	 */
	public boolean checkLogin(String token) {
		if(StringUtils.isEmpty(token)){
			 return false;
		} 
		String customerId = null;
		try {
		      customerId = loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY+token);
		} catch (Exception e) {
			 logger.error("出现异常checkLoginToken():"+e.getMessage());
			 return false;
		}
		if(StringUtils.isEmpty(customerId)){
			 return false;
		}
		return true;
	}
	
	/**
	 * 获取缓存客户Id
	 * @param token
	 * @return
	 */
	public String getTokenId(String token) { 
		String customerId =null;
		try {
			customerId = loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY+token);
			if(StringUtils.isEmpty(customerId)){
				customerId ="0";
			}
		} catch (Exception e) {
			customerId = "0";
			logger.error("出现异常getTokenId():"+e.getMessage());
		}
		return customerId;
	}
	
	
	
	/**
	 * 单点登录缓存token
	 * @param id
	 * @return
	 */
	public String setLoginToken(String mobile,String imei,String id){
		String mdMobile = MD5Utils.getMD5String(mobile);
		String mdImei = MD5Utils.getMD5String(imei);
		String token = mdMobile+"@"+mdImei;
		loginTokenRedis.opsForValue().set(VPP_LOGIN_KEY+mdMobile, mdImei);
		loginTokenRedis.opsForValue().set(VPP_IMEI_KEY+mdImei, id);
		return token; 
	}
	 
	/**
	 * 校验单点登录状态
	 * @param token
	 * @return
	 */
	public boolean checkLoginToken(String token) {
		if(StringUtils.isEmpty(token)){
			 return false;
		}
		String [] arry = token.split("@");
		String mdMobile = arry[0];
		String mdImei = arry[1]; 
		String resultImei = loginTokenRedis.opsForValue().get(VPP_LOGIN_KEY+mdMobile);
		if(!mdImei.equals(resultImei)){
			return false;
		} 
		return true;
	}

	/**
	 * 获取缓存客户Id 单点
	 * @param token
	 * @return
	 */
	public String getLoginTokenId(String token) { 
		String [] arry = token.split("@");
		//String mdMobile = arry[0];
		String mdImei = arry[1];
		String customerId=(String)loginTokenRedis.opsForValue().get(VPP_IMEI_KEY+mdImei);
		return customerId;
	}

	/**
	 * 国际化语言取值
	 * @param msgKey
	 * @return
	 */
	public String getMessage(String msgKey) {
		Locale locale = LocaleContextHolder.getLocale();
		String msg = messageSource.getMessage(msgKey, null, locale);
		return msg;
	}
	
	/**
	 * 缓存手机验证码
	 * @param mobile
	 * @param code
	 */
	public void setMobileCodeRedis(String mobile,String code){
		mobileCodeTokenRedis.opsForValue().set(LOGIN_VPP_MOBILE_CODE+mobile+code,code, MOBILE_CODE_CACHE_THIRTY_MINUTE, TimeUnit.SECONDS);
	}

	/**
	 * 校验手机验证码
	 * @param mobile
	 * @param code
	 * @return
	 */
	public boolean checkMobileCode(String mobile,String code) {
		String getCode = LOGIN_VPP_MOBILE_CODE+mobile+code;
		getCode = mobileCodeTokenRedis.opsForValue().get(getCode);
		if (getCode == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * del login key
	 * @param token
	 * @return
	 */
	public boolean delLoginToken(String token){
		try {
			loginTokenRedis.delete(VPP_LOGIN_KEY+token);
		} catch (Exception e) {
			logger.error("出现异常delLoginToken():"+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 退出
	 * @param token 登录token
	 * @return
	 */
	public boolean loginOut(String token,String mobile){
		boolean bool = true;
		try {
			String [] arry = token.split("@");
			String mdMobile = arry[0];
			String mdImei = arry[1];
			loginTokenRedis.delete(VPP_LOGIN_KEY+mdMobile);
			loginTokenRedis.delete(VPP_IMEI_KEY+mdImei);
		} catch (Exception e) {
			bool = false;
			logger.error("出现异常loginOut()"+e.getMessage());
		}
		return bool;
	}
	
}
