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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.vo.DepositVo;
import com.vpp.core.common.EthController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.deposit.bean.Deposit;
import com.vpp.core.deposit.mapper.DepositMapper;
import com.vpp.core.deposit.service.IDepositService;

@Service
public class DepositService implements IDepositService {
    private static final Logger logger = LogManager.getLogger(DepositService.class);
    @Autowired
    private DepositMapper depositMapper;
    @Autowired
    private CustomerMapper customerMapper;

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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int vppPay(Map<String, Object> params) {
        int ret = 1;
        try {
            String account = (String) params.get("account");
            Customer customer = customerMapper.selectByAddress(account);
            Gson gson = new Gson();
            // String json = HttpUtils.post(EthController.QUERY_VPP_DEPOSIT_URL,params);
            // ResultVo result = gson.fromJson(json, ResultVo.class);
            Object data = EthController.queryVppDeposit(params);
            if (data != null) {
                List<DepositVo> list = gson.fromJson(gson.toJson(data), new TypeToken<List<DepositVo>>() {
                }.getType());
                BigDecimal sum = null;
                Long customerId = customer.getId();
                for (DepositVo depositVo : list) {
                    BigDecimal value = depositVo.getValue();
                    Deposit deposit = new Deposit();
                    deposit.setCustomerId(customerId);
                    deposit.setCashNo(depositVo.getGasUsed());
                    deposit.setPayeeAddress(depositVo.getTo());// 收款账户
                    deposit.setPayerAddress(depositVo.getFrom()); // 支付账户
                    deposit.setTimastamp(new Long(depositVo.getTimeStamp() + "000"));
                    deposit.setFromBlock(depositVo.getBlockNumber());
                    deposit.setVpp(value);
                    deposit.setGmtCreate(new Date());
                    if (depositMapper.insertSelective(deposit) > 0) {
                        sum = DealUtil.priceAdd(value, sum);
                    }
                }
                BigDecimal balance = customer.getBalance();
                customer = new Customer();
                customer.setId(customerId);
                customer.setBalance(DealUtil.priceAdd(balance, sum));
                customerMapper.updateByPrimaryKeySelective(customer);
            }
        } catch (Exception e) {
            ret = 0;
            logger.error("发生异常：" + e.getMessage());
        }
        return ret;
    }

    @Override
    public Long getMaxBlockNumber(String account) {
        return depositMapper.getMaxBlockNumber(account);
    }

}
