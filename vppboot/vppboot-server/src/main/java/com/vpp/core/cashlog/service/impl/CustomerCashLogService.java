package com.vpp.core.cashlog.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.cashlog.bean.CustomerCashLog;
import com.vpp.core.cashlog.mapper.CustomerCashLogMapper;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;

@Service
public class CustomerCashLogService implements ICustomerCashLogService {

    private static final Logger logger = LogManager.getLogger(CustomerCashLogService.class);

    @Autowired
    private CustomerCashLogMapper customerCashLogMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public int insertDeposit(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_DEPOSIT);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertWithdrawal(Long customerId, BigDecimal amount, String desc) throws Exception {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(new BigDecimal(-amount.doubleValue()));
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_WITHDRAWAL);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertPay(Long customerId, BigDecimal amount, String desc) throws Exception {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(new BigDecimal(-amount.doubleValue()));
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_PAY);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertPayout(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_PAYOUT);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertInvite(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_INVITE);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertRegister(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_RIGESTER);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertWithdrawalReject(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_WITHDRAWAL_REJECT);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    @Override
    public int insertWithdrawalFail(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_WITHDRAWAL_FAIL);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    public int income(Long customerId, BigDecimal amount, String desc) {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(amount);
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_INCOME);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        // 累加资金
        this.addBalance(customerId, amount, cashLog);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    public int expenditure(Long customerId, BigDecimal amount, String desc) throws Exception {
        CustomerCashLog cashLog = new CustomerCashLog();
        cashLog.setAmount(new BigDecimal(-amount.doubleValue()));
        cashLog.setAmountType(ConstantsServer.CashLog.AMOUNT_TYPE_EXPENDITURE);
        cashLog.setCustomerId(customerId);
        cashLog.setDescription(desc);
        // 删减资金
        this.subtractBalance(customerId, amount, cashLog);
        return customerCashLogMapper.insertSelective(cashLog);
    }

    /**
     * 根据会员ID查询会员
     * 
     * @author Lxl
     * @param customerId
     * @return
     */
    private BigDecimal getBalance(Long customerId) {
        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        return customer.getBalance();
    }

    /**
     * 删减资金并判断账号余额是否足够
     * 
     * @author Lxl
     * @param customerId
     * @param amount
     * @param cashLog
     * @throws Exception
     */
    private void subtractBalance(Long customerId, BigDecimal amount, CustomerCashLog cashLog) throws Exception {
        BigDecimal balance = this.getBalance(customerId);
        BigDecimal newBalance = DealUtil.priceSubtract(balance, amount);
        if (newBalance.floatValue() < 0) {
            logger.error("客户ID:::{} 余额不足，异常支付，账户余额:::{}，删减金额:::{}", customerId, balance, amount);
            throw new Exception("insufficient funds");
        } else {
            cashLog.setBalance(newBalance);
        }
    }

    /**
     * 累加资金
     * 
     * @author Lxl
     * @param customerId
     * @param amount
     * @param cashLog
     */
    private void addBalance(Long customerId, BigDecimal amount, CustomerCashLog cashLog) {
        BigDecimal balance = this.getBalance(customerId);
        BigDecimal newBalance = DealUtil.priceAdd(balance, amount);
        cashLog.setBalance(newBalance);
    }

    @Override
    public Page<CustomerCashLog> findLimitByCondition(Integer currentPage, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(currentPage, pageSize);
        return customerCashLogMapper.findLimitByCondition(params);
    }
}
