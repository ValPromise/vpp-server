package com.vpp.core.deposit;

import java.math.BigDecimal;
import java.util.Map;

import com.github.pagehelper.Page;

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
	Page<Deposit> selectDepositInfo(int pageNum,int pageSize,Map<String,Object> map);

	int vppPay(Map<String, Object> params,Long customerId,BigDecimal balance);
	
	Long getMaxBlockNumber(String account);
}
