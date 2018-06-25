package com.vpp.core.customer.app;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.common.GeetestHandle;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.deposit.service.IDepositService;
import com.vpp.core.withdrawal.bean.WithdrawalAccount;
import com.vpp.core.withdrawal.service.IWithdrawalService;

@RestController
@RequestMapping("/app/customer")
public class AppCustomerController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppCustomerController.class);
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IDepositService depositService;
    @Autowired
    private IWithdrawalService withdrawalService;
    // 极验安全验证
    @Autowired
    private GeetestHandle geetestHandle;

    /** 生成二维码链接地址 **/
    private String CREATE_QR_CODE_URL = "http://qr.liantu.com/api.php";

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
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            logger.error("token不存在 ::: {}",token);
            return ResultVo.setResultError();
        }
        // 异步调用查询用户充值记录接口
        this.syncDepositByAccount(customer.getDepositAddress());
        customer = customerService.selectCustomerById(new Long(customerId));
        Map<String, Object> map = new HashMap<String, Object>();
        BigDecimal balance = customer.getBalance();
        map.put("id", customerId);
        map.put("balance", balance.toString());
        map.put("cashAmount", customerService.getVppValue(balance));
        return ResultVo.setResultSuccess(map);
    }

    // 任务3;

    @Async
    public void syncDepositByAccount(String account) {
        // 查询充值记录入库
        depositService.syncDepositByAccount(account);
    }

    /**
     * 查询用户钱包可用余额
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/balance")
    public ResultVo getBalance(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        Customer customer = customerService.selectCustomerById(new Long(getCustomerIdByToken(token)));
        if (customer == null) {
            return ResultVo.setResultError();
        }
        List<WithdrawalAccount> list = withdrawalService.selectWithdrawalAccountByCustomerId(customer.getId());
        int exist = 1;
        if (CollectionUtils.isEmpty(list)) {
            exist = 0;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("balance", customer.getBalance());
        map.put("accountExist", exist);
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
        // if (StringUtils.isEmpty(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        String password = customer.getPassword();
        String payPassword = customer.getPayPassword();
        if (!StringUtils.isEmpty(password)) {
            customer.setPassword(MD5Utils.getMD5String(password));
        }
        if (!StringUtils.isEmpty(payPassword)) {
            customer.setPayPassword(MD5Utils.getMD5String(payPassword));
        }
        customer.setId(new Long(getCustomerIdByToken(token)));
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
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }
        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        String walletAddress = customer.getDepositAddress();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", customer.getId());
        map.put("walletCode", CREATE_QR_CODE_URL + "?text=" + walletAddress);
        map.put("walletAddress", walletAddress);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 判断是否存在支付密码
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/isExistPayPassword")
    public ResultVo isExistPayPassword(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }
        Long customerId = new Long(getCustomerIdByToken(token));
        Customer customer = customerService.selectCustomerById(customerId);
        if (customer == null) {
            return ResultVo.setResultError();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        String password = customer.getPayPassword();
        if (StringUtils.isEmpty(password)) {
            map.put("exist", 0);
            return ResultVo.setResultSuccess(map);
        } else {
            map.put("exist", 1);
            return ResultVo.setResultSuccess(map);
        }
    }

    /**
     * 校验支付密码是否正确
     * 
     * @author Lxl
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

        if (StringUtils.isBlank(payPassword)) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        // 匹配成功
        if (customer.getPayPassword().equals(MD5Utils.getMD5String(payPassword))) {
            map.put("success", 1);
            return ResultVo.setResultSuccess(map);
        } else {
            map.put("success", 0);
            return ResultVo.setResultSuccess(map);
        }

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

        if (StringUtils.isBlank(newPayPassword)) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }

        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }

        // 校验旧密码是否匹配，如果没有旧支付密码则直接修改
        String payPasswordTemp = customer.getPayPassword();
        if (StringUtils.isNotBlank(payPasswordTemp)) {
            if (StringUtils.isBlank(oldPayPassword)) {
                return ResultVo.setResultError(getMessage("parameter_null"));
            }
            oldPayPassword = MD5Utils.getMD5String(oldPayPassword);
            if (!payPasswordTemp.equals(oldPayPassword)) {
                return ResultVo.setResultError(getMessage("old_pay_password"));
            }
        }

        // 校验新设置的支付密码规则
        String regexPayPassword = "^[0-9]{6}$";
        boolean bool = Pattern.matches(regexPayPassword, newPayPassword);
        if (!bool) {
            return ResultVo.setResultError(getMessage("pay_password_error"));
        }

        try {
            newPayPassword = MD5Utils.getMD5String(newPayPassword);
            customerService.updatePayPassword(customer.getId(), newPayPassword);
        } catch (Exception e) {
            return ResultVo.setResultError(getMessage("update_pay_password_fail"));
        }
        return ResultVo.setResultSuccess(getMessage("update_pay_password_success"));
    }

    /**
     * 忘记支付密码，重新设置
     * 
     * @author Lxl
     * @param token
     * @param code
     * @param newPayPassword
     * @param response
     * @return
     */
    @RequestMapping("/forgetPayPassword")
    public ResultVo forgetPayPassword(String token, String code, String newPayPassword, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (StringUtils.isBlank(newPayPassword)) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }

        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }

        // 校验手机验证码是否正确
        if (!checkMobileCode(customer.getMobile(), code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }

        // 校验新设置的支付密码规则
        String regexPayPassword = "^[0-9]{6}$";
        boolean bool = Pattern.matches(regexPayPassword, newPayPassword);
        if (!bool) {
            return ResultVo.setResultError(getMessage("pay_password_error"));
        }

        try {
            newPayPassword = MD5Utils.getMD5String(newPayPassword);
            customerService.updatePayPassword(customer.getId(), newPayPassword);
        } catch (Exception e) {
            return ResultVo.setResultError(getMessage("update_pay_password_fail"));
        }
        return ResultVo.setResultSuccess(getMessage("update_pay_password_success"));
    }

    /**
     * 修改登录密码
     * 
     * @param token 登录验证
     * @param payPassword 密码
     * @param response
     * @return
     */
    @RequestMapping("/updatePassword")
    public ResultVo updatePassword(String token, String oldPassword, String newPassword, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isBlank(newPassword)) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }

        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }

        // 校验旧密码是否匹配，如果没有旧支付密码则直接修改
        String passwordTemp = customer.getPassword();
        if (StringUtils.isNotBlank(passwordTemp)) {
            if (StringUtils.isBlank(oldPassword)) {
                return ResultVo.setResultError(getMessage("parameter_null"));
            }
            oldPassword = MD5Utils.getMD5String(oldPassword);
            if (!passwordTemp.equals(oldPassword)) {
                return ResultVo.setResultError(getMessage("old_pay_password"));
            }
        }

        if (newPassword.length() < 6) {
            return ResultVo.setResultError(getMessage("parameter_error"));
        }

        try {
            newPassword = MD5Utils.getMD5String(newPassword);
            // 修改密码
            customerService.updatePassword(customer.getId(), newPassword);
        } catch (Exception e) {
            return ResultVo.setResultError(getMessage("update_pay_password_fail"));
        }
        return ResultVo.setResultSuccess(getMessage("update_pay_password_success"));
    }

    /**
     * 忘记登录密码，重新设置
     * 
     * @author Lxl
     * @param token
     * @param code
     * @param newPassword
     * @param response
     * @return
     */
    @RequestMapping("/forgetPassword")
    public ResultVo forgetPassword(String mobile, String code, String newPassword, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isBlank(newPassword)) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        Customer customer = customerService.selectCustomerByMobile(mobile);
        if (customer == null) {
            return ResultVo.setResultError();
        }

        // 校验手机验证码是否正确
        if (!checkMobileCode(customer.getMobile(), code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }

        if (newPassword.length() < 6) {
            return ResultVo.setResultError(getMessage("parameter_error"));
        }

        try {
            newPassword = MD5Utils.getMD5String(newPassword);
            // 修改密码
            customerService.updatePassword(customer.getId(), newPassword);
        } catch (Exception e) {
            return ResultVo.setResultError(getMessage("update_pay_password_fail"));
        }
        return ResultVo.setResultSuccess(getMessage("update_pay_password_success"));
    }

    /**
     * 修改手机号码
     * 
     * @author Lxl
     * @return
     */
    @RequestMapping("/updateMobile")
    public ResultVo updateMobile(String token, String code, String newMobile, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        String customerId = getCustomerIdByToken(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }

        // 是否通过安全验证
        // boolean suc = geetestHandle.isSuccess(customer.getMobile());
        // if (!suc) {
        // return ResultVo.setResultError(getMessage("geetest_validate_fail"));
        // }

        if (StringUtils.isBlank(code) || StringUtils.isBlank(newMobile)) {
            return ResultVo.setResultError(getMessage("parameter_null"));
        }

        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }

        // 校验手机验证码是否正确
        if (!checkMobileCode(newMobile, code)) {
            return ResultVo.setResultError(getMessage("mobile_check_error"));
        }

        // 判断手机号码是否存在
        Customer t = customerService.selectCustomerByMobile(newMobile);
        if (t != null) {
            return ResultVo.setResultError(getMessage("mobile_used"));
        }
        try {
            customerService.updateMobile(customer.getId(), newMobile);
            // 删除登录缓存，重新登录
            delLoginToken(token);
        } catch (Exception e) {
            return ResultVo.setResultError(getMessage("update_fail"));
        }
        return ResultVo.setResultSuccess(getMessage("update_success"));
    }

    /**
     * 查询会员邀请信息
     * 
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/inviteCodePage")
    public ResultVo inviteCodePage(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
//        if (!checkLogin(token)) {
//            return ResultVo.setResultError(getMessage("token"));
//        }
        Customer customer = customerService.selectCustomerById(new Long(getCustomerIdByToken(token)));
        if (customer == null) {
            return ResultVo.setResultError();
        }
        Integer inviteUp = customer.getInviteUp();
        if (null == inviteUp) {
            inviteUp = 0;
        }

        String inviteCode = customer.getInviteCode();
        int count = customerService.countInviteCode(inviteCode);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("inviteCode", inviteCode);
        map.put("inviteUp", inviteUp);
        map.put("noUseInviteCode", 0 > inviteUp - count ? 0 : inviteUp - count);
        map.put("qrCode", CREATE_QR_CODE_URL + "?text=" + inviteCode);

        // #########系统参数动态配置-----app分享title
        // params.app.share.title=
        // #########系统参数动态配置-----app分享content
        // params.app.share.content=
        // #########系统参数动态配置-----app分享icon url
        // params.app.share.icon=
        map.put("shareTitle", ConstantsServer.Share.SHARE_TITLE);
        map.put("shareContent", ConstantsServer.Share.SHARE_CONTENT);
        map.put("shareIcon", ConstantsServer.Share.SHARE_ICON);
        map.put("shareUrl", ConstantsServer.Share.SHARE_URL);
        return ResultVo.setResultSuccess(map);
    }

}
