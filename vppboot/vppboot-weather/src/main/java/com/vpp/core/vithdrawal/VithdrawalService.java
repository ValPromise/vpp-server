package com.vpp.core.vithdrawal;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vpp.common.page.PageDataResult;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.customer.Customer;
import com.vpp.core.customer.CustomerMapper;

@Service
public class VithdrawalService implements IVithdrawalService {

	private static final Logger logger = LogManager
			.getLogger(VithdrawalService.class);
	/** 成功 **/
	private final int STATE_SUCCESS = 2;

	/** 打回 **/
	private final Byte STATE_BACK = 3;

	/** 失败 **/
	private final int STATE_ERROR = 4;

	@Autowired
	private VithdrawalMapper vithdrawalMapper;
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertVithdrawal(Vithdrawal vithdrawal) {
		vithdrawal.setGmtCreate(new Date());
		Long customerId = vithdrawal.getCustomerId();
		int ret = 1;
		try {
			Customer customer = customerMapper.selectByPrimaryKey(customerId);
			if (customer == null) {
				return 0;
			}
			BigDecimal vpp = vithdrawal.getVpp();
			BigDecimal balance = customer.getBalance();
			if (DealUtil.priceCompare(vpp, balance, ">")) {
				ret = 0;
			} else {
				vithdrawalMapper.insertSelective(vithdrawal);
				Customer ctm = new Customer();
				ctm.setId(customerId);
				ctm.setBalance(DealUtil.priceSubtract(balance,vpp));
				customerMapper.updateByPrimaryKeySelective(ctm);
			}
		} catch (Exception e) {
			ret = 0;
			logger.error(e.getMessage());
		}
		return ret;
	}

	@Override
	public int vithdrawalOperation(Vithdrawal vithdrawal) {
		int ret = 1;
		Date date = new Date();
		vithdrawal.setGmtModified(date);
		vithdrawal.setOperationTime(date);
		try {
			vithdrawalMapper.updateByPrimaryKeySelective(vithdrawal);
		} catch (Exception e) {
			ret = 0;
			logger.error(e.getMessage());
		}
		return ret;
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int vithdrawalBack(Long id) {
		int ret = 1;
		Date date = new Date();
		try {
			Vithdrawal vithdrawal = new Vithdrawal();
			vithdrawal.setGmtModified(date);
			vithdrawal.setOperationTime(date);
			vithdrawal.setState(STATE_BACK);
			vithdrawal.setDescription("打回。。。。。");
			vithdrawal.setOperationDesc("打回。。。。");
			vithdrawal.setOperatorId(1l);
			vithdrawal.setId(id);
			Vithdrawal vdw = vithdrawalMapper.selectByPrimaryKey(id);
			vithdrawalMapper.updateByPrimaryKeySelective(vithdrawal);
			Customer customer = customerMapper.selectByPrimaryKey(vdw.getCustomerId());
			if (customer == null) {
				return 0;
			}
			Customer ctm = new Customer();
			ctm.setGmtModified(date);
			ctm.setId(vdw.getCustomerId());
			ctm.setBalance(DealUtil.priceAdd(customer.getBalance(),vdw.getVpp()));
			customerMapper.updateByPrimaryKeySelective(ctm);
		} catch (Exception e) {
			ret = 0;
			logger.error(e.getMessage());
		}
		return ret;
	}

	@Override
	public PageDataResult selectVithdrawalInfo(int pageNum,int pageSize,Map<String, Object> map) {
		PageHelper.startPage(pageNum, pageSize);
		List<Vithdrawal> rows = vithdrawalMapper.selectVithdrawalInfo(map);
		PageDataResult result = new PageDataResult();
		result.setRows(rows);
		PageInfo<Vithdrawal> pageInfo = new PageInfo<Vithdrawal>(rows);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

}
