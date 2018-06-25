package com.vpp.core.deposit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.vo.DepositVo;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.common.EthController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.deposit.bean.Deposit;
import com.vpp.core.deposit.mapper.DepositAccountMapper;
import com.vpp.core.deposit.mapper.DepositMapper;
import com.vpp.core.deposit.service.IDepositService;

@Service
public class DepositService implements IDepositService {
    private static final Logger logger = LogManager.getLogger(DepositService.class);
    @Autowired
    private DepositMapper depositMapper;
    @Autowired
    private DepositAccountMapper depositAccountMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ICustomerCashLogService customerCashLogService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertDeposit(Deposit deposit) {
        deposit.setGmtCreate(new Date());
        Long customerId = deposit.getCustomerId();
        int ret = 1;
        try {
            Customer customer = customerMapper.selectByPrimaryKey(customerId);
            if (customer != null) {
                depositMapper.insertSelective(deposit);
                Customer ctm = new Customer();
                ctm.setId(customerId);
                ctm.setBalance(DealUtil.priceAdd(customer.getBalance(), deposit.getVpp()));
                customerMapper.updateByPrimaryKeySelective(ctm);
            } else {
                ret = 0;
            }
        } catch (Exception e) {
            ret = 0;
            logger.error(e.getMessage());
        }
        return ret;
    }

    @Override
    public Page<Map<String, Object>> selectDepositInfo(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Map<String, Object>> rows = depositMapper.selectDepositInfo(map);
        return rows;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void syncDepositByAccount(String account) {
        // 查询最大区块号
        Long fromBlock = depositMapper.getMaxBlockNumber(account);
        fromBlock = null == fromBlock ? 0l : fromBlock;

        List<DepositVo> list = EthController.queryVppDeposit(account, fromBlock);
        if (list != null) {
            BigDecimal sum = null;
            Customer customer = customerMapper.selectByAddress(account);
            for (DepositVo depositVo : list) {
                BigDecimal value = depositVo.getValue();
                String hash = depositVo.getTransactionHash();// 链上交易hashk ey
                Deposit deposit = new Deposit();
                deposit.setCustomerId(customer.getId());
                deposit.setCashNo(hash);
                deposit.setPayerAddress(depositVo.getFrom()); // 支付账户
                deposit.setPayeeAddress(depositVo.getTo());// 收款账户
                deposit.setTimastamp(new Long(depositVo.getTimeStamp() + "000"));
                deposit.setFromBlock(depositVo.getBlockNumber());
                deposit.setVpp(value);
                deposit.setGmtCreate(new Date());
                depositMapper.insertSelective(deposit);

                logger.info("---查询到充值记录：：： {}", depositVo);
                // 充值流水
                sum = DealUtil.priceAdd(value, sum);
                customerCashLogService.insertDeposit(customer.getId(), value, hash + "充值");
            }
            if (null != sum) {
                BigDecimal balance = customer.getBalance();
                Customer customerTemp = new Customer();
                customerTemp.setId(customer.getId());
                customerTemp.setBalance(DealUtil.priceAdd(balance, sum));

                // 累加客户充值钱包余额
                depositAccountMapper.updateBalanceByAccount(account, sum.intValue());
                // 更新客户账户余额
                customerMapper.updateByPrimaryKeySelective(customerTemp);

            }
        }
    }

    @Override
    public Page<Deposit> findLimit(Integer currentPage, Integer pageSize, Map<String, Object> map) {
        PageHelper.startPage(currentPage, pageSize);
        return depositMapper.findLimit(map);
    }
}
