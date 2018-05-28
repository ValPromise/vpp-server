package com.vpp.core.vithdrawal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.page.PageDataResult;
import com.vpp.common.utils.StringUtils;
import com.vpp.core.common.CommonController;
import com.vpp.vo.ResultVo;
/**
 * 提现
 * @author EDZ
 *
 */
@RestController
@RequestMapping("/vithdrawal")
public class VithdrawalController extends CommonController{
	
	private static final Logger logger = LogManager.getLogger(VithdrawalController.class);

	@Autowired
	private IVithdrawalService vithdrawalService;
	
	@RequestMapping("/vithdrawalList")
	public ResultVo selectVithdrawalInfo(Integer pageNum,Integer pageSize,String token,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(StringUtils.isEmpty(pageNum)){
			pageNum = 1;
		}
		if(StringUtils.isEmpty(pageSize)){
			pageSize = 15;
		}
		if(StringUtils.isEmpty(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		}
		if(!checkLogin(token)){
			 return ResultVo.setResultError(getMessage("token"),TOKEN_FAIL_ERROR_CODE);
		}
		Map<String, Object> map = new  HashMap<String, Object>();
		PageDataResult result = vithdrawalService.selectVithdrawalInfo(pageNum, pageSize, map);
		return  ResultVo.setResultSuccess(result);
	}
	

}
