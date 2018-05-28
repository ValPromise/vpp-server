package com.vpp.core.deposit.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.deposit.bean.Deposit;

public interface IDepositService {
	
	/**
	 * vpp充值
	 * @param deposit
	 * @return
	 */
	int insertDeposit(Deposit deposit);
	
	/**
	 * VPP充值记录查询
	 * @param pageNum
	 * @param pageSize
	 * @param map
	 * @return
	 */
	Page<Map<String, Object>> selectDepositInfo(int pageNum,int pageSize,Map<String,Object> map);

	int vppPay(Map<String, Object> params);
	
	Long getMaxBlockNumber(String account);
}
