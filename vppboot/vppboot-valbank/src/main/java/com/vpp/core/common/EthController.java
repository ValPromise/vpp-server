package com.vpp.core.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.vpp.common.utils.HttpUtils;
import com.vpp.vo.ResultVo;

@Component
public class EthController {

	private static final Logger logger = LogManager
			.getLogger(EthController.class);

	public static String IP;
 
	public  String getIP() {
		return IP;
	}

	@Value("${vpp.eth.create.url}")
	public  void setIP(String iP) {
		EthController.IP = iP;
	}

	/**eth充值记录URL**/
	public static  String QUERY_ETH_TX_LIST_URL = "/token/queryEthTxList";
	/** eth开盘价接口URL **/
	public static  String QUERY_POLONIEX_ETH_TICKER_URL ="/token/queryPoloniexEthTicker";
	/** 生成钱包地址UR **/
	public static  String CREATE_ACCOUNT_UR = "/token/createAccount";
 
	/**
	 * 生成钱包地址接口
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String createAccount() {
		String account = null;
		Gson gson = new Gson();
		try {
			String result = HttpUtils.post(IP+CREATE_ACCOUNT_UR,
					new HashMap<String, Object>());
			ResultVo resultVo = gson.fromJson(result, ResultVo.class);
			if (resultVo != null) {
				Map<String, Object> map = (Map<String, Object>) resultVo
						.getData();
				account = (String) map.get("accountId");
			}
		} catch (Exception e) {
			logger.error("发生异常:" + e.getMessage());
		}
		return account;
	}
	
	/**
	 *  eth开盘价接口
	 * @return
	 */
	public static ResultVo queryPoloniexEthTicker(){ 
		String data = HttpUtils.post(IP+QUERY_POLONIEX_ETH_TICKER_URL,new HashMap<String, Object>());
		Gson gson = new Gson();
		ResultVo resultVo = gson.fromJson(data,ResultVo.class);
		return resultVo;
	}

	/**
	 * vpp充值记录 接口
	 * @param params
	 * @return
	 */
	public static Object queryVppDeposit(Map<String, Object> params){
		 Gson gson = new Gson();
		 String json = HttpUtils.post(IP+QUERY_ETH_TX_LIST_URL,params);
		 ResultVo result = gson.fromJson(json, ResultVo.class);
		 return result.getData();
	}
	
	
    
	
}
