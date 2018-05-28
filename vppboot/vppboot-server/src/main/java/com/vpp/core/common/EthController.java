package com.vpp.core.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.SecurityUtils;
import com.vpp.common.vo.ResultVo;

@Component
public class EthController {

    private static final Logger logger = LogManager.getLogger(EthController.class);

    public static String IP;

    public String getIP() {
        return IP;
    }

    @Value("${vpp.eth.create.url}")
    public void setIP(String iP) {
        EthController.IP = iP;
    }

    /** vpp充值记录URL **/
    public static String QUERY_VPP_DEPOSIT_URL = "/token/queryVppDeposit";
    /** eth开盘价接口URL **/
    public static String QUERY_POLONIEX_ETH_TICKER_URL = "/token/queryPoloniexEthTicker";
    /** 生成钱包地址UR **/
    public static String CREATE_ACCOUNT_URL = "/token/createAccount";

    /** VPP提现 **/
    public static String TRANSFER_URL = "/external/bc/transfer";

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
            Map<String, String> params = new HashMap<String, String>();
            params.put("password", Constants.DEFAULT_ACCOUNT_PASSWORD);
            params.put("sign", SecurityUtils.getSign(params));

            String result = HttpUtils.postString(IP + CREATE_ACCOUNT_URL, params);
            ResultVo resultVo = gson.fromJson(result, ResultVo.class);
            if (resultVo != null) {
                Map<String, Object> map = (Map<String, Object>) resultVo.getData();
                account = (String) map.get("accountId");
            }
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
        }
        return account;
    }

    /**
     * eth开盘价接口
     * 
     * @return
     */
    public static ResultVo queryPoloniexEthTicker() {
        String data = HttpUtils.post(IP + QUERY_POLONIEX_ETH_TICKER_URL, new HashMap<String, Object>());
        Gson gson = new Gson();
        ResultVo resultVo = gson.fromJson(data, ResultVo.class);
        return resultVo;
    }

    /**
     * vpp充值记录 接口
     * 
     * @param params
     * @return
     */
    public static Object queryVppDeposit(Map<String, Object> params) {
        Gson gson = new Gson();
        String json = HttpUtils.post(IP + QUERY_VPP_DEPOSIT_URL, params);
        ResultVo result = gson.fromJson(json, ResultVo.class);
        return result.getData();
    }

    /**
     * VPP提现
     * 
     * @param params
     * @return
     */
    public static ResultVo transfer(String to, Integer amount) {
        if (StringUtils.isBlank(to) || null == amount) {
            return ResultVo.setResultError("params is wrong");
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("to", to);
        params.put("amount", amount.toString());
        params.put("sign", SecurityUtils.getSign(params));

        String json = HttpUtils.postString(IP + TRANSFER_URL, params);
        ResultVo result = new Gson().fromJson(json, ResultVo.class);
        return result;
    }
}
