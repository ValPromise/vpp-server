package com.vpp.core.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.SecurityUtils;
import com.vpp.common.vo.DepositVo;
import com.vpp.common.vo.ResultVo;

import net.sf.json.JSONObject;

@Component
public class EthController {

    private static final Logger logger = LogManager.getLogger(EthController.class);

    public static String IP;
    // withdrawal.pay.account=0x300BAB56a81c096A21f2075964f095D635658834
    // withdrawal.pay.account.password=2016xttqb
    @Value("${withdrawal.pay.account}")
    public String payAccount;
    @Value("${withdrawal.pay.account.password}")
    public String payAccountPassword;

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

    // queryVppWithdrawal(String payer, Integer fromBlock,String payee)
    /**
     * VPP提现记录链上查询
     */
    public static String QUERY_VPP_WITHDRAWAL_URL = "/token/queryVppWithdrawal";

    /**
     * 查询火币最新行情，国军提供 {"code":0,"price":602.4}
     */
    // public static String HUOBI_ETH_USDT_URL = "http://47.75.91.177:8001/";
    public static String HUOBI_ETH_USDT_URL = "http://119.28.70.201:8001/";

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
     * eth行情价
     * 
     * @return
     */
    public static ResultVo queryPoloniexEthTicker() {
        // String data = HttpUtils.post(IP + QUERY_POLONIEX_ETH_TICKER_URL, new HashMap<String, Object>());
        String data = HttpUtils.get(HUOBI_ETH_USDT_URL, new HashMap<String, String>());
        JSONObject huoEthUsdt = JSONObject.fromObject(data);
        return ResultVo.setResultSuccess(huoEthUsdt.getDouble("price"));
    }

    /**
     * vpp充值记录 接口
     * 
     * @param params
     * @return
     */
    public static List<DepositVo> queryVppDeposit(String account, Long fromBlock) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("fromBlock", fromBlock.toString());
        Gson gson = new Gson();
        String json = HttpUtils.post(IP + QUERY_VPP_DEPOSIT_URL, params);
        ResultVo result = gson.fromJson(json, ResultVo.class);

        List<DepositVo> list = gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<DepositVo>>() {
        }.getType());
        return list;
    }

    /**
     * VPP提现记录查询
     * 
     * @param params
     * @return
     */
    public static List<DepositVo> queryVppWithdrawal(String payer, Long fromBlock, String payee) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("payer", payer);
        params.put("fromBlock", fromBlock.toString());
        params.put("payee", payee);
        Gson gson = new Gson();
        String json = HttpUtils.post(IP + QUERY_VPP_WITHDRAWAL_URL, params);
        ResultVo result = gson.fromJson(json, ResultVo.class);

        List<DepositVo> list = gson.fromJson(gson.toJson(result.getData()), new TypeToken<List<DepositVo>>() {
        }.getType());
        return list;
    }

    /**
     * VPP提现
     * 
     * @param params
     * @return
     */
    public ResultVo transfer(String to, Double amount) throws Exception {
        if (StringUtils.isBlank(to) || null == amount) {
            return ResultVo.setResultError("params is wrong");
        }
        Map<String, String> params = new HashMap<String, String>();
        // String account, String password, String to, Double amount
        params.put("account", payAccount);
        params.put("password", payAccountPassword);
        params.put("to", to);
        params.put("amount", amount.toString());
        params.put("sign", SecurityUtils.getSign(params));
        logger.debug("params ::: {}", params);
        String json = HttpUtils.postString(IP + TRANSFER_URL, params);
        ResultVo result = new Gson().fromJson(json, ResultVo.class);
        return result;
    }
}
