package com.vpp.core.withdrawal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.withdrawal.bean.Withdrawal;
import com.vpp.core.withdrawal.bean.WithdrawalAccount;
import com.vpp.core.withdrawal.mapper.WithdrawalAccountMapper;
import com.vpp.core.withdrawal.mapper.WithdrawalMapper;
import com.vpp.core.withdrawal.service.IWithdrawalService;

@Service
public class WithdrawalService implements IWithdrawalService {
	
	private static final Logger logger = LogManager.getLogger(WithdrawalService.class);
	
	@Autowired
	private WithdrawalAccountMapper withdrawalAccountMapper;
	@Autowired
	private WithdrawalMapper withdrawalMapper;
	@Autowired
	private CustomerMapper customerMapper;
	
	@Override
	public int insertWithdrawalAccount(WithdrawalAccount withdrawalAccount) {
		int ret = 0;
		try {
			ret = withdrawalAccountMapper.insertSelective(withdrawalAccount);
		} catch (Exception e) {
			ret = 0;
			logger.error("发生异常:"+e.getMessage());
		}
		return ret;
	}

	@Override
	public List<WithdrawalAccount> selectWithdrawalAccountByCustomerId(Long customerId) {
		List<WithdrawalAccount> list = null;
		try {
			list =withdrawalAccountMapper.selectWithdrawalAccountByCustomerId(customerId);
		} catch (Exception e) {
			logger.error("发生异常:"+e.getMessage());
		}
		return list;
	}

	@Override
	public int updateWithdrawalAccount(WithdrawalAccount withdrawalAccount) {
		int ret = 1;
		try {
			ret = withdrawalAccountMapper.updateByPrimaryKeySelective(withdrawalAccount);
		} catch (Exception e) {
			ret = 0;
			logger.error("发生异常:"+e.getMessage());
		}
		return ret;
	}

	@Override
	public Page<Withdrawal> selectWithdrawalList(int pageNum, int pageSize,Map<String, Object> map) {
		PageHelper.startPage(pageNum,pageSize);
		Page<Withdrawal> rows = withdrawalMapper.selectWithdrawalList(map);
		return rows;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class) 
	public int withdrawal(Withdrawal withdrawal) {
		int ret = 1;
		try {
	        Long customerId =  withdrawal.getCustomerId();
			withdrawalMapper.insertSelective(withdrawal);
			Customer customer = customerMapper.selectByPrimaryKey(customerId);
			Customer ctm = new Customer();
			ctm.setId(customerId);
			ctm.setBalance(DealUtil.priceSubtract(customer.getBalance(),withdrawal.getVpp()));
			customerMapper.updateByPrimaryKeySelective(ctm);
		} catch (Exception e) {
			ret = 0;
			logger.error("出现异常:"+e.getMessage());
		}
		return ret;
	}

}
