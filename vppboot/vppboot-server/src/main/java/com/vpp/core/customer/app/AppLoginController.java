package com.vpp.core.customer.app;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.SendSmsUtil;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.common.EthController;
import com.vpp.core.common.GeetestHandle;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.deposit.bean.DepositAccount;
import com.vpp.core.deposit.service.IDepositAccountService;

@RestController
@RequestMapping("/app")
public class AppLoginController extends CommonController {

    private static final Logger logger = LogManager.getLogger(AppLoginController.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IDepositAccountService depositAccountService;
    @Autowired
    private GeetestHandle geetestHandle;
    // 获取配置文件 development , production , test
    @Value("${spring.profiles.active}")
    private String profilesActive;
    /**
     * 邀请人数上限，修改邀请人数上限为5000 20180621
     */
    public static final Integer INVITE_UP = 5000;

    /**
     * 获取手机验证码
     * 
     * @param mobile
     * @param response
     * @return
     */
    @RequestMapping("/getMobileCode")
    public ResultVo getMobileCode(String mobile, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // 是否通过安全验证
        logger.debug("mobile::{}", mobile);
        boolean suc = geetestHandle.isSuccess(mobile);
        if (!suc) {
            return ResultVo.setResultError(getMessage("geetest_validate_fail"));
        }

        if (StringUtils.isEmpty(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_null"));
        }
        if (!SendSmsUtil.isMobile(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_format_error"));
        }
        String code = SendSmsUtil.getCaptcha();
        logger.debug("code:::{}", code);
        // sendSmsChuangLan
        String result = null;
        if (0 == mobile.indexOf("86")) {
            // 发送普通短信
            result = SendSmsUtil.sendSmsChuangLan(mobile.substring(2), code);
        } else {
            // 国际短信
            result = SendSmsUtil.sendSmsIntl(mobile, code);
        }

        if (result.equals("fail")) { // 短信发送失败
            return ResultVo.setResultError(getMessage("send_sms_fail"));
        }
        setMobileCodeRedis(mobile, code);
        logger.info("手机验证码：" + mobile + "\t" + code);
        return ResultVo.setResultSuccess(result);
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
    public ResultVo register(Customer customer, String code, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        // 是否通过安全验证
        boolean suc = geetestHandle.isSuccess(customer.getMobile());
        if (!suc) {
            return ResultVo.setResultError(getMessage("geetest_validate_fail"));
        }

        String mobile = customer.getMobile(); // 手机号码
        String inviteCode = customer.getInviteCode(); // 邀请码
        String password = customer.getPassword();
        if (StringUtils.isEmpty(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_null"));
        }
        if (!SendSmsUtil.isMobile(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_format_error"));
        }
        if (!checkMobileCode(mobile, code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }
        if (StringUtils.isEmpty(password)) {
            return ResultVo.setResultError(getMessage("login_password_null"));
        }
        Customer t = customerService.selectCustomerByMobile(mobile);
        if (t != null) {
            if (mobile.equals(t.getMobile())) { // 手机号已被注册
                return ResultVo.setResultError(getMessage("mobile_used"));
            }
        }
        Customer inviteCustomer = null;
        if (!StringUtils.isBlank(inviteCode)) { // 没有邀请码，自动生成一个邀请码
            inviteCustomer = customerService.selectByCode(inviteCode);
            if (inviteCustomer == null) {
                return ResultVo.setResultError(getMessage("invitation_code_error"));
            } else {
                int count = customerService.countInviteCode(inviteCode);
                if (count >= inviteCustomer.getInviteUp()) { // 邀请码达到上线
                    // return ResultVo.setResultError(getMessage("invitation_code_finish"));
                    inviteCustomer = null;
                }
                customer.setInviteFrom(inviteCode);
            }
        }
        // 生成充值钱包
        String account = EthController.createAccount();
        if (StringUtils.isEmpty(account)) {
            return ResultVo.setResultError(getMessage("create_account_fail"));
        }
        if (customerService.getCustomerByAccount(account) != null) {
            return ResultVo.setResultError(getMessage("create_account_fail"));
        }

        //邀请码非空验证
        String newInviteCode = "";
        Customer tempCus = null;
        do {
            newInviteCode = getInviteCode(5);
            tempCus = customerService.selectByCode(newInviteCode);
        } while (null != tempCus);

        customer.setInviteCode(newInviteCode);
        customer.setDepositAddress(account);
        customer.setPassword(MD5Utils.getMD5String(password));
        customer.setInviteUp(INVITE_UP);
        customer.setBalance(new BigDecimal(0));
        customer.setGmtCreate(new Date());

        try {
            DepositAccount depositAccount = new DepositAccount();
            depositAccount.setCustomerId(customer.getId());
            depositAccount.setGmtCreate(new Date());
            depositAccount.setAccount(account);
            customerService.register(customer, depositAccount, inviteCustomer);

            return ResultVo.setResultSuccess();
        } catch (Exception e) {
            logger.error("register error :::{}", e.getMessage());
        }
        return ResultVo.setResultError(getMessage("register_error"));
    }

    /**
     * 登录-密码登录
     * 
     * @param mobile 手机号码
     * @param code 手机验证码
     * @param imei 手机串码
     * @param response
     * @return
     */
    @RequestMapping("/login")
    public ResultVo login(String mobile, String password, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // 判断是否通过安全验证，正式环境需要打开
        boolean suc = geetestHandle.isSuccess(mobile);
        if (!suc && "production".equals(profilesActive)) {
            return ResultVo.setResultError(getMessage("geetest_validate_fail"));
        }

        if (!SendSmsUtil.isMobile(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_format_error"));
        }
        if (StringUtils.isEmpty(password)) {
            return ResultVo.setResultError(getMessage("login_password_null"));
        }
        Customer customer = customerService.selectCustomerByMobile(mobile);
        if (customer == null) { // 账号不存在，请注册
            return ResultVo.setResultError(getMessage("account_no"));
        }
        if (!MD5Utils.getMD5String(password).equals(customer.getPassword())) {
            return ResultVo.setResultError(getMessage("login_password_error"));
        }
        String token = setLoginToken(customer.getId().toString()); // 登录缓存token
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        // 判断是否已经设置支付密码
        if (StringUtils.isNotBlank(customer.getPayPassword())) {
            map.put("payPasswordExist", 1);
        } else {
            map.put("payPasswordExist", 0);
        }
        if (StringUtils.isNotBlank(customer.getPassword())) {
            map.put("passwordExist", 1);
        } else {
            map.put("passwordExist", 0);
        }

        ResultVo resultVo = ResultVo.setResultSuccess("登录成功", map);
        logger.info("登录返回结果" + new Gson().toJson(resultVo));
        return resultVo;
    }

    /**
     * 登录-验证码登录
     * 
     * @param mobile 手机号码
     * @param code 手机验证码
     * @param imei 手机串码
     * @param response
     * @return
     */
    @RequestMapping("/loginCode")
    public ResultVo loginCode(String mobile, String code, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // 判断是否通过安全验证，正式环境需要打开
        boolean suc = geetestHandle.isSuccess(mobile);
        if (!suc) {
            return ResultVo.setResultError(getMessage("geetest_validate_fail"));
        }
        if (!SendSmsUtil.isMobile(mobile)) {
            return ResultVo.setResultError(getMessage("mobile_format_error"));
        }
        if (!checkMobileCode(mobile, code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }
        Customer customer = customerService.selectCustomerByMobile(mobile);
        if (customer == null) { // 账号不存在，请注册
            return ResultVo.setResultError(getMessage("account_no"));
        }
        String token = setLoginToken(customer.getId().toString()); // 登录缓存token
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        // 判断是否已经设置支付密码
        if (StringUtils.isNotBlank(customer.getPayPassword())) {
            map.put("payPasswordExist", 1);
        } else {
            map.put("payPasswordExist", 0);
        }
        if (StringUtils.isNotBlank(customer.getPassword())) {
            map.put("passwordExist", 1);
        } else {
            map.put("passwordExist", 0);
        }

        ResultVo resultVo = ResultVo.setResultSuccess("登录成功", map);
        logger.info("登录返回结果" + new Gson().toJson(resultVo));
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
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        boolean bool = delLoginToken(token);
        if (!bool) {
            return ResultVo.setResultError("fail");
        }
        return ResultVo.setResultSuccess("success");
    }
}
