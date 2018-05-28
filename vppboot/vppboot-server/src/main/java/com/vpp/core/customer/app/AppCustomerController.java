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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.MD5Utils;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.deposit.app.AppDepositController;
import com.vpp.core.withdrawal.bean.WithdrawalAccount;
import com.vpp.core.withdrawal.service.IWithdrawalService;

@RestController
@RequestMapping("/app/customer")
public class AppCustomerController extends CommonController {
    private static final Logger logger = LogManager.getLogger(AppCustomerController.class);
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private AppDepositController appDepositController;
    @Autowired
    private IWithdrawalService  withdrawalService;
    
    /**生成二维码链接地址**/
    private String CREATE_QR_CODE_URL = "http://qr.liantu.com/api.php";
   
    /**
     * 账户详情
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
        String customerId = getTokenId(token);
        Customer customer = customerService.selectCustomerById(new Long(customerId));
        if (customer == null) {
            return ResultVo.setResultError();
        }
        String walletAddress = customer.getDepositAddress();
        appDepositController.vppPay(walletAddress, response); // 查询充值记录入库
        customer = customerService.selectCustomerById(new Long(customerId));
        Map<String, Object> map = new HashMap<String, Object>();
        BigDecimal balance = customer.getBalance();
        map.put("id", customerId);
        map.put("balance", balance);
        map.put("cashAmount", customerService.getVppValue(balance));
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 可用余额
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/balance")
    public ResultVo getBalance(String token,HttpServletResponse response) {
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
		if(CollectionUtils.isEmpty(list)){
			isShow = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("balance", customer.getBalance());
		map.put("isShow", isShow);
		return ResultVo.setResultSuccess(map);
    }

    /**
     * 修改会员信息
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
        String walletAddress = customer.getDepositAddress();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", customer.getId());
        map.put("walletCode", CREATE_QR_CODE_URL+"?text=" + walletAddress);
        map.put("walletAddress", walletAddress);
        return ResultVo.setResultSuccess(map);
    }

    /**
     * 校验是否设置支付密码
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
        Long customerId = new Long(getTokenId(token));
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
        Long customerId = new Long(getTokenId(token));
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
        if (!password.equals(payPassword)) {
            return ResultVo.setResultError("支付密码有误");
        }
        return ResultVo.setResultSuccess();
    }
    
    /**
     * 邀请有奖
     * @param token
     * @param response
     * @return
     */
    @RequestMapping("/inviteCodePage")
    public ResultVo inviteCodePage(String token,HttpServletResponse response){
    	 response.addHeader("Access-Control-Allow-Origin","*");
    	 if (!checkLogin(token)) {
             return ResultVo.setResultError(getMessage("token"));
         }
    	 Customer customer = customerService.selectCustomerById(new Long(getTokenId(token)));
         if(customer==null){
        	 return ResultVo.setResultError();
         }
         int inviteUp = customer.getInviteUp();
         String inviteCode = customer.getInviteCode();
         int count = customerService.countInviteCode(inviteCode);
         Map<String,Object> map = new HashMap<String, Object>();
         map.put("inviteCode", inviteCode);
         map.put("inviteUp", inviteUp);
         map.put("noUseInviteCode", inviteUp-count);
         map.put("qrCode", CREATE_QR_CODE_URL+"?text="+inviteCode);
    	 return ResultVo.setResultSuccess(map);
    }
    
}
