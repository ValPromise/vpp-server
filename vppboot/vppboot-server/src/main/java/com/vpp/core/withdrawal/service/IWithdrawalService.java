package com.vpp.core.withdrawal.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.withdrawal.bean.Withdrawal;
import com.vpp.core.withdrawal.bean.WithdrawalAccount;

public interface IWithdrawalService {
	
	/**
	 * 新增钱包地址
	 * @param withdrawalAccount
	 * @return
	 */
	int insertWithdrawalAccount(WithdrawalAccount withdrawalAccount);
	
	/**
	 * 根据客户Id查询钱包地址
	 * @param customerId
	 * @return
	 */
	List<WithdrawalAccount> selectWithdrawalAccountByCustomerId(Long customerId);
	
	/**
	 * 修改提现钱包地址
	 * @param withdrawalAccount
	 * @return
	 */
	int updateWithdrawalAccount(WithdrawalAccount withdrawalAccount);

	/**
	 * 提现记录
	 * @param map
	 * @return
	 */
	Page<Withdrawal> selectWithdrawalList(int pageNum, int pageSize,Map<String, Object> map);

	/**
	 * 提现
	 * @param withdrawal
	 * @return
	 */
	int withdrawal(Withdrawal withdrawal);
}
