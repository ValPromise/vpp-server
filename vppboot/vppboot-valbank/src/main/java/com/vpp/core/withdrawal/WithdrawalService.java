package com.vpp.core.withdrawal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalService implements IWithdrawalService{

	private static final Logger logger = LogManager.getLogger(WithdrawalController.class);

	@Autowired
	private WithdrawalMapper withdrawalMapper;
	
	@Override
	public int withdrawalEth(Withdrawal withdrawal) {
		int ret =1;
		try {
		  ret=withdrawalMapper.insertSelective(withdrawal);
		} catch (Exception e) {
		  ret = 0;
		  logger.error("发生异常："+e.getMessage());
		}
		return ret;
	}

}
