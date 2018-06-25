package com.vpp.core.cashlog.service;

import java.math.BigDecimal;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.cashlog.bean.CustomerCashLog;

/**
 * 资金流水
 * 
 * @author Lxl
 * @version V1.0 2018年6月6日
 */
public interface ICustomerCashLogService {
    /**
     * 充值流水
     * 
     * @author Lxl
     * @param amount
     * @param desc
     * @return
     */
    int insertDeposit(Long customerId, BigDecimal amount, String desc);

    /**
     * 提现流水
     * 
     * @author Lxl
     * @param amount
     * @param desc
     * @return
     */
    int insertWithdrawal(Long customerId, BigDecimal amount, String desc) throws Exception;

    /**
     * 提现流水拒绝，恢复账户金额
     * 
     * @author Lxl
     * @param amount
     * @param desc
     * @return
     */
    int insertWithdrawalReject(Long customerId, BigDecimal amount, String desc);

    /**
     * 提现打款失败，恢复账户金额
     * 
     * @author Lxl
     * @param amount
     * @param desc
     * @return
     */
    int insertWithdrawalFail(Long customerId, BigDecimal amount, String desc);

    /**
     * 产品购买支付流水
     * 
     * @author Lxl
     * @param amount
     * @param desc
     * @return
     */
    int insertPay(Long customerId, BigDecimal amount, String desc) throws Exception;

    /**
     * 产品赔付流水
     * 
     * @author Lxl
     * @param amount
     * @param desc
     * @return
     */
    int insertPayout(Long customerId, BigDecimal amount, String desc);

    /**
     * 邀请赠送资金流水
     * 
     * @author Lxl
     * @param customerId
     * @param amount
     * @param desc
     * @return
     */
    int insertInvite(Long customerId, BigDecimal amount, String desc);

    /**
     * 注册用户赠送资金流水
     * 
     * @author Lxl
     * @param customerId
     * @param amount
     * @param desc
     * @return
     */
    int insertRegister(Long customerId, BigDecimal amount, String desc);

    /**
     * 收入资金
     * 
     * @author Lxl
     * @param customerId
     * @param amountType
     * @param amount
     * @param desc
     * @return
     */
    int income(Long customerId, BigDecimal amount, String desc);

    /**
     * 支出资金
     * 
     * @author Lxl
     * @param customerId
     * @param amountType
     * @param amount
     * @param desc
     * @return
     */
    int expenditure(Long customerId, BigDecimal amount, String desc) throws Exception;

    Page<CustomerCashLog> findLimitByCondition(Integer currentPage, Integer pageSize, Map<String, Object> params)
            throws Exception;

}
