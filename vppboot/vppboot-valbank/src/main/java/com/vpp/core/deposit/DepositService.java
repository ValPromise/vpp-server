package com.vpp.core.deposit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.core.common.EthController;
import com.vpp.core.customer.Customer;
import com.vpp.core.customer.CustomerMapper;
import com.vpp.vo.ApiTx;
@Service
public class DepositService implements IDepositService{
	private static final Logger logger = LogManager.getLogger(DepositService.class);
	@Autowired
	private DepositMapper depositMapper;
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private RedisTemplate<String, String> depositServiceRedis;
	
	public static final String VALBANK_RATE_KEY = "valbank_rate_key_"; 
	
	public static final int VALBANK_RATE_KEY_TIME = 1;//60*60*24; //1小时

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertDeposit(Deposit deposit) {
		deposit.setGmtCreate(new Date());
		Long customerId = deposit.getCustomerId();
		int ret = 1;
		try {
		  Customer	customer =  customerMapper.selectByPrimaryKey(customerId);
		  if(customer!=null){
			  depositMapper.insertSelective(deposit);
			  Customer ctm = new Customer();
			  ctm.setId(customerId);
			  ctm.setBalance(DealUtil.priceAdd(customer.getBalance(),deposit.getAmount()));
			  customerMapper.updateByPrimaryKeySelective(ctm);
		  }else{
			  ret = 0; 
		  }
		} catch (Exception e) {
			ret= 0;
			logger.error(e.getMessage());
		}
		return ret;
	}

	@Override
	public Page<Deposit> selectDepositInfo(int pageNum, int pageSize,
			Map<String, Object> map) {
		PageHelper.startPage(pageNum, pageSize);
		Page<Deposit> rows = depositMapper.selectDepositInfo(map); 
		return rows;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public int vppPay(Map<String, Object> params) {
		int ret = 1;
		try { 
			 String account = (String) params.get("account");
			 Customer customer =  customerMapper.selectByAddress(account);
			 Gson gson = new Gson();
 			 Object data =EthController.queryVppDeposit(params);
			 if(data!=null){
				 List<ApiTx> list =gson.fromJson(gson.toJson(data), new TypeToken<List<ApiTx>>() {}.getType());
				 BigDecimal sum = null;
				 Long customerId = customer.getId();
				 for (ApiTx depositVo : list) {
					 BigDecimal value =new BigDecimal(depositVo.getValue());
					 Deposit deposit = new Deposit();
					 deposit.setCustomerId(customerId);
					 deposit.setCashNo(depositVo.getGasUsed());
					 deposit.setToAccount(depositVo.getTo());//收款账户
					 deposit.setFromAccount(depositVo.getFrom()); //支付账户
					 deposit.setTimastamp(new Long(depositVo.getTimeStamp()+"000"));
					 deposit.setFromBlock(new Long(depositVo.getBlockNumber()));
					 deposit.setAmount(value);
					 deposit.setGmtCreate(new Date());
					 if(depositMapper.insertSelective(deposit)>0){
						 sum=DealUtil.priceAdd(value,sum);
					 }
				 }
				 BigDecimal balance = customer.getBalance();
				 customer = new Customer();
				 customer.setId(customerId);
				 customer.setBalance(DealUtil.priceAdd(balance,sum));
				 customerMapper.updateByPrimaryKeySelective(customer);
			 }
		} catch (Exception e) {
			ret=0;
			logger.error("发生异常："+e.getMessage());
		}
		return ret;
	}

	@Override
	public Long getMaxBlockNumber(String account) {
		return depositMapper.getMaxBlockNumber(account);
	}

	@Override
	public BigDecimal depositListById(Long customerId,Float rate) {
		String cacheKey = VALBANK_RATE_KEY+DateUtil.format(new Date(), DateUtil.YMD_DATE_TIME_PATTERN)+customerId;
		String rateKeyValue  = depositServiceRedis.opsForValue().get(cacheKey);
		BigDecimal sum = new BigDecimal(0);
		if(rateKeyValue==null){
		    List<Deposit> list = depositMapper.selectDepositListById(customerId);
			if(list!=null){
				for(Deposit deposit:list){
					int day =	DateUtil.diffDate(new Date(),new Date(deposit.getTimastamp()));
					BigDecimal value = deposit.getAmount().multiply(new BigDecimal(rate.toString())).multiply(new BigDecimal(day)).divide(new BigDecimal(365),18,BigDecimal.ROUND_DOWN);
					sum = DealUtil.priceAdd(value,sum);
				}
				depositServiceRedis.opsForValue().set(cacheKey,sum.toString(), VALBANK_RATE_KEY_TIME, TimeUnit.DAYS);
			}
		}else{
			return new BigDecimal(rateKeyValue);
		}
		return sum;
	}
	
}
