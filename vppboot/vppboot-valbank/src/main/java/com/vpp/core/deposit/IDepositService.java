package com.vpp.core.deposit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.vo.DepositVo;

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

	int vppPay(Map<String, Object> params);
	
	Long getMaxBlockNumber(String account);
	
	BigDecimal depositListById(Long customerId,Float rate);
}
