package com.vpp.core.customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.filter.BlackUtil;
import com.vpp.common.filter.SendSmsUtil;
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.StringUtils;
import com.vpp.core.common.CommonController;
import com.vpp.core.common.EthController;
import com.vpp.core.deposit.IDepositService;
import com.vpp.core.withdrawal.IWithdrawalService;
import com.vpp.core.withdrawal.WithdrawalAccount;
import com.vpp.vo.ResultVo;

@RestController
@RequestMapping("/customer")
public class CustomerController extends CommonController {
    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IWithdrawalService withdrawalService;
    @Autowired
    private IDepositService depositService;

    /**
     * 验证邀请码
     * 
     * @param code
     * @param response
     * @return
     */
    @RequestMapping("/checkCode")
    public ResultVo checkCode(String invitationCode, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isEmpty(invitationCode)) {
            return ResultVo.setResultError(getMessage("invitation_code_null"));
        }
        Customer ctm = customerService.selectByCode(invitationCode);
        if (ctm == null) {
            return ResultVo.setResultError(getMessage("invitation_code_error"));
        }
        if (!StringUtils.isEmpty(ctm.getMobile())) {
            return ResultVo.setResultError(getMessage("invitation_code_used"));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("invitationCode", invitationCode);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 获取手机验证码
     * 
     * @param response
     * @return
     */
    @RequestMapping("/getCode")
    public ResultVo getCode(String mobile, HttpServletResponse response,HttpServletRequest request) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        String ip = this.getRemoteAddress(request);
        if(null == ip){
            BlackUtil.printError(response);
            return null;
        }
        String code = SendSmsUtil.getCaptcha();
        logger.info("手机号:mobile={}" + mobile + "\t" + "验证码：code={}" + code);
        String result = SendSmsUtil.sendSmsChuangLan(mobile, code);
        if (result.equals("fail")) { // 短信发送失败
            return ResultVo.setResultError(getMessage("send_sms_fail"));
        }
        setMobileCodeRedis(mobile, code); // 验证码缓存5分钟
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code); // 返回手机验证码
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 注册
     * 
     * @param mobile
     * @param code
     * @param response
     * @return
     */
    @RequestMapping("/register")
    public synchronized ResultVo register(String mobile, String invitationCode, String code, HttpServletResponse response,
            HttpServletRequest request) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (!SendSmsUtil.isMobile(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_format_error"));
        }
        if (!checkMobileCode(mobile, code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }
        // Customer customer=customerService.selectByCode(invitationCode);
        // if(customer==null){
        // return ResultVo.setResultError(getMessage("invitation_code_error"));
        // }
        // if(!StringUtils.isEmpty(customer.getMobile())){
        // return ResultVo.setResultError(getMessage("invitation_code_used"));
        // }
        Customer ct = customerService.selectCustomerByUserName(mobile);
        if (ct != null) {
            if (mobile.equals(ct.getMobile())) { // 手机号已被注册
                return ResultVo.setResultError(getMessage("mobile_used"));
            }
        }
        String account = EthController.createAccount();
        if (StringUtils.isEmpty(account)) {
            return ResultVo.setResultError(getMessage("create_account_fail"));
        }
        if (customerService.getCustomerByAccount(account) != null) {
            return ResultVo.setResultError(getMessage("create_account_fail"));
        }
        Customer customer = new Customer();
        customer.setMobile(mobile);
        customer.setWalletAddress(account); // 内部钱包地址
        customer.setBalance(new BigDecimal(30));
        int ret = customerService.register(customer);
        if (ret > 0) {
            String token = setLoginToken(customer.getId().toString());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            return ResultVo.setResultSuccess(getMessage("register_success"), map);
        }
        return ResultVo.setResultError(getMessage("register_error"));
    }

    public static final String IP_HASH = "ip_hash";

    private String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();

        for (String ipTemp : ip.split(",")) {
            // if(null != ipTemp && ipTemp.equals("175.168.31.231")){
            // logger.info("Blacklist to ::: {}", ipTemp);
            // return null;
            // }
//            logger.info("Blacklist to ::: {},{}", ipTemp,redisTemplate);
            if (null != ipTemp && null != redisTemplate) {
                System.out.println("-----------" + redisTemplate);
                if (redisTemplate.hasKey(IP_HASH + ipTemp)) {
                    Integer count = Integer.parseInt("" + redisTemplate.opsForValue().get(IP_HASH + ipTemp));
                    if (count.intValue() > 5) {
                        redisTemplate.opsForValue().set(IP_HASH + ipTemp, "" + (count.intValue() + 1), Constants.CACHE_TIME_DAY,
                                TimeUnit.SECONDS);
                        logger.info("Blacklist to ::: {}", ipTemp);
                        return null;
                    }
                    redisTemplate.opsForValue().set(IP_HASH + ipTemp, "" + (count.intValue() + 1), Constants.CACHE_TIME_MINUTE,
                            TimeUnit.SECONDS);

                    // redis.opsForHash().put(IP_HASH, ipTemp, 1);
                } else {
                    redisTemplate.opsForValue().set(IP_HASH + ipTemp, "" + 1, Constants.CACHE_TIME_MINUTE, TimeUnit.SECONDS);
                    // redis.opsForHash().put(IP_HASH, ipTemp, count.intValue() + 1);
                }
            }
        }
        return ip;
    }

    /**
     * 登录
     * 
     * @param mobile 手机号码
     * @param code 手机验证码
     * @param response
     * @return
     */
    @RequestMapping("/login")
    public synchronized ResultVo login(String mobile, String code, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!SendSmsUtil.isMobile(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_format_error"));
        }
        if (!checkMobileCode(mobile, code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }
        Customer customer = customerService.selectCustomerByUserName(mobile);
        if (customer == null) { // 账号不存在，请注册
            String account = EthController.createAccount();
            if (StringUtils.isEmpty(account)) {
                return ResultVo.setResultError(getMessage("create_account_fail"));
            }
            if (customerService.getCustomerByAccount(account) != null) {
                return ResultVo.setResultError(getMessage("create_account_fail"));
            }
            customer = new Customer();
            customer.setMobile(mobile);
            customer.setWalletAddress(account); // 内部钱包地址
            customer.setBalance(new BigDecimal(30));
            customerService.register(customer);
        }
        logger.info("时间：" + System.currentTimeMillis() + "\t 请求" + mobile);
        String token = setLoginToken(customer.getId().toString()); // 登录缓存token
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        ResultVo resultVo = ResultVo.setResultSuccess(map);
        return resultVo;
    }

    /**
     * 退出
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/loginOut")
    public ResultVo loginOut(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        boolean bool = delLoginToken(token);
        if (!bool) {
            return ResultVo.setResultError("fail");
        }
        return ResultVo.setResultSuccess("success");
    }

    /**
     * 账户详情
     * 
     * @param customer
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/customerDetail")
    public ResultVo customerDetail(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        Long customerId = new Long(getTokenId(token));
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String walletAddress = customer.getWalletAddress();
        Long fromBlock = depositService.getMaxBlockNumber(walletAddress); // 获取最大区块号
        if (fromBlock == null) {
            fromBlock = 0L;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", walletAddress);
        params.put("fromBlock", fromBlock.toString());
        depositService.vppPay(params, customerId, customer.getBalance());
        customer = customerService.selectCustomerById(customerId);
        BigDecimal balance = customer.getBalance();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", customerId);
        map.put("balance", balance);
        map.put("cashAmount", customerService.getVppValue(balance));
        map.put("walletAddress", walletAddress);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 可用余额
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/balance")
    public ResultVo getBalance(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        Customer customer = customerService.selectCustomerById(new Long(getTokenId(token)));
        if (customer == null) {
            return ResultVo.setResultError();
        }
        List<WithdrawalAccount> list = withdrawalService.selectWithdrawalAccountByCustomerId(customer.getId());
        int isShow = 1;
        if (list.size() == 0) {
            isShow = 0;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("balance", customer.getBalance());
        map.put("isShow", isShow);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 修改会员信息
     * 
     * @param customer
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/updateCustomer")
    public ResultVo updateCustomer(Customer customer, String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isEmpty(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        String password = customer.getPassword();
        String payPassword = customer.getPayPassword();
        if (!StringUtils.isEmpty(password)) {
            customer.setPassword(MD5Utils.getMD5String(password));
        }
        if (!StringUtils.isEmpty(payPassword)) {
            customer.setPayPassword(MD5Utils.getMD5String(payPassword));
        }
        customer.setId(new Long(getTokenId(token)));
        int ret = customerService.updateCustomer(customer);
        return ret > 0 ? ResultVo.setResultSuccess("修改成功") : ResultVo.setResultError("修改失败");
    }

    /**
     * 获取内部钱包地址
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/getWalletAddress")
    public ResultVo getWalletAddress(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        String customerId = getTokenId(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        String url = "https://api.qrserver.com/v1/create-qr-code/";
        String walletAddress = customer.getWalletAddress();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", customer.getId());
        map.put("walletCode", url + "?size=1500x150&data=" + walletAddress);
        map.put("walletAddress", walletAddress);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 校验是否绑定支付密码
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/checkWhetherPayPassword")
    public ResultVo checkWhetherPayPassword(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }
        String id = getTokenId(token);
        Long customerId = new Long(id);
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String password = customer.getPayPassword();
        if (StringUtils.isEmpty(password)) {
            return ResultVo.setResultError("请设置支付密码。");
        }
        return ResultVo.setResultSuccess();
    }

    /**
     * 设置-修改支付密码
     * 
     * @param token 登录验证
     * @param payPassword 支付密码
     * @param response
     * @return
     */
    @RequestMapping("/setPayPassword")
    public ResultVo setPayPassword(String token, String payPassword, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }
        String id = getTokenId(token);
        Long customerId = new Long(id);
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String password = customer.getPayPassword();
        if (!StringUtils.isEmpty(password)) {
            return ResultVo.setResultError(getMessage("again_set_pay_password"));
        }
        String regexPayPassword = "^[0-9]{6}$";
        boolean bool = Pattern.matches(regexPayPassword, payPassword);
        if (!bool) {
            return ResultVo.setResultError(getMessage("pay_password_error"));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", customerId);
        map.put("payPassword", MD5Utils.getMD5String(payPassword));
        int ret = customerService.updatePayPassword(map);
        return ret > 0 ? ResultVo.setResultSuccess(getMessage("set_pay_password_success"))
                : ResultVo.setResultError(getMessage("set_pay_password_fail"));
    }

    /**
     * 检验旧支付密码
     * 
     * @param token
     * @param payPassword
     * @param response
     * @return
     */
    @RequestMapping("/checkOldPayPassword")
    public ResultVo checkOldPayPassword(String token, String payPassword, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }
        String regexPayPassword = "^[0-9]{6}$";
        boolean bool = Pattern.matches(regexPayPassword, payPassword);
        if (!bool) {
            return ResultVo.setResultError(getMessage("pay_password_error"));
        }
        String id = getTokenId(token);
        Long customerId = new Long(id);
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String password = customer.getPayPassword();
        String oldPayPassword = MD5Utils.getMD5String(payPassword);
        if (!password.equals(oldPayPassword)) {
            return ResultVo.setResultError(getMessage("old_pay_password"));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("oldPayPassword", oldPayPassword);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 修改支付密码
     * 
     * @param token 登录验证
     * @param payPassword 密码
     * @param response
     * @return
     */
    @RequestMapping("/updatePayPassword")
    public ResultVo updatePayPassword(String token, String oldPayPassword, String newPayPassword,
            HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }
        String regexPayPassword = "^[0-9]{6}$";
        boolean bool = Pattern.matches(regexPayPassword, newPayPassword);
        if (!bool) {
            return ResultVo.setResultError(getMessage("pay_password_error"));
        }
        String id = getTokenId(token);
        Long customerId = new Long(id);
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String password = customer.getPayPassword();
        if (!password.equals(oldPayPassword)) {
            return ResultVo.setResultError(getMessage("old_pay_password"));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", customerId);
        map.put("payPassword", MD5Utils.getMD5String(newPayPassword));
        int ret = customerService.updatePayPassword(map);
        return ret > 0 ? ResultVo.setResultSuccess(getMessage("update_pay_password_success"))
                : ResultVo.setResultError(getMessage("update_pay_password_fail"));
    }

    /**
     * 校验支付密码
     * 
     * @param token
     * @param payPassword
     * @param response
     * @return
     */
    @RequestMapping("/checkPayPassword")
    public ResultVo checkPayPassword(String token, String payPassword, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }
        String regexPayPassword = "^[0-9]{6}$";
        boolean bool = Pattern.matches(regexPayPassword, payPassword);
        if (!bool) {
            return ResultVo.setResultError(getMessage("pay_password_error"));
        }
        String id = getTokenId(token);
        Long customerId = new Long(id);
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String password = customer.getPayPassword();
        if (!password.equals(MD5Utils.getMD5String(payPassword))) {
            return ResultVo.setResultError("支付密码有误");
        }
        return ResultVo.setResultSuccess("正确支付密码");
    }
}
