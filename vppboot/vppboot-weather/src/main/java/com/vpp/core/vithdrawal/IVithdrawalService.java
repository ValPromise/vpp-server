package com.vpp.core.vithdrawal;

import java.util.Map;

import com.vpp.common.page.PageDataResult;

public interface IVithdrawalService {
	/**
	 * vpp提现
	 * 
	 * @param vithdrawal
	 * @return
	 */
	int insertVithdrawal(Vithdrawal vithdrawal);

	/**
	 * 提现处理
	 * 
	 * @param vithdrawal
	 * @return
	 */
	int vithdrawalOperation(Vithdrawal vithdrawal);

	/**
	 * 提现打回
	 * 
	 * @param vithdrawal
	 * @return
	 */
	int vithdrawalBack(Long id);

	/**
	 * VPP提现记录查询
	 * 
	 * @return
	 */
	PageDataResult selectVithdrawalInfo(int pageNum,int pageSize,Map<String, Object> map);
}
