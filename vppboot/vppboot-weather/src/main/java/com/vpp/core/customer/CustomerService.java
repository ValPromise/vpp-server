package com.vpp.core.customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.StringUtils;
import com.vpp.core.common.EthController;
import com.vpp.vo.ResultVo;

@Service
public class CustomerService implements ICustomerService {
	
	private static final Logger logger = LogManager.getLogger(CustomerService.class);

	/**汇率URL**/
	private final String FROM_USD_TO_CNY_URL="http://119.28.70.201:9961/chinaNum";
	
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private Gson gson;
	@Autowired
	private RedisTemplate<String, String> customerServiceRedis;
	
	public static final String ETH_OPEN_VALUE_KEY = "eth_open_value_key";
	public static final int ETH_OPEN_VALUE_KEY_TIME = 10; //10秒
	public static final String FROM_USD_TO_CNY_KEY = "from_usd_to_cny_key"; 
	public static final int FROM_USD_TO_CNY_TIME = 60*60; //1小时

	@Override
	public  int register(Customer customer) {
		customer.setGmtCreate(new Date());
		return customerMapper.insertSelective(customer);
	}

	@Override
	public Customer selectCustomerById(Long id) {
		return customerMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateCustomer(Customer customer) {
		return customerMapper.updateByPrimaryKeySelective(customer);
	}

	@Override
	public Customer selectCustomerByUserName(String userName) {
		return customerMapper.selectCustomerByUserName(userName);
	}

    @Override
    public int updateCustomerBalance(Long id,BigDecimal addBalance) {
        return customerMapper.updateCustomerBalance(id,addBalance);
    }

	@Override
	public Customer selectByCode(String invitationCode) {
		return customerMapper.selectByCode(invitationCode);
	}

	@Override
	public String getWalletAddress(Long id) {
		return customerMapper.getWalletAddress(id);
	}
	
	
	/**
	 * 获取eth开盘价
	 * @return 开盘价
	 */
	@SuppressWarnings("unchecked")
	public Double getEthOpenValue(){
		Double openValue = 0D;
		try {
			String  ethOpenValue  = customerServiceRedis.opsForValue().get(ETH_OPEN_VALUE_KEY);
			if(StringUtils.isEmpty(ethOpenValue)){
//				System.out.println(EthController.QUERY_POLONIEX_ETH_TICKER_URL);
//				String data = HttpUtils.post(EthController.QUERY_POLONIEX_ETH_TICKER_URL,new HashMap<String, Object>());
				ResultVo resultVo = EthController.queryPoloniexEthTicker();
						//gson.fromJson(data,ResultVo.class);
				if(resultVo!=null){
					Map<String, Object> map = (Map<String, Object>)resultVo.getData();
					Object objValue = map.get("open");
					if(objValue!=null){
						openValue = (Double) objValue;   
					}
				} 
				customerServiceRedis.opsForValue().set(ETH_OPEN_VALUE_KEY,openValue.toString(),ETH_OPEN_VALUE_KEY_TIME,TimeUnit.SECONDS);
			}else{
				openValue = Double.valueOf(ethOpenValue);
			}
		} catch (Exception e) {
			logger.error("发生异常 getVppOpenValue() :"+e.getMessage());
		}
		return openValue;
	}
	
	/**
	 * 美元 ->人民币
	 * @return
	 */
	public Double getFromUsdToCny(){
		Double result = 0D;
		try {
			String  cnyValue   = customerServiceRedis.opsForValue().get(FROM_USD_TO_CNY_KEY);
			if(StringUtils.isEmpty(cnyValue)){
				String  data = HttpUtils.get(FROM_USD_TO_CNY_URL,new HashMap<String, String>());
			    Map<String, Object> map = gson.fromJson(data,new TypeToken<Map<String, Object>>() {}.getType());
			    Double value =Double.valueOf(map.get("result").toString());
			    result =value/100;
			    customerServiceRedis.opsForValue().set(FROM_USD_TO_CNY_KEY,result.toString(),FROM_USD_TO_CNY_TIME,TimeUnit.SECONDS);
			}else{
				result = Double.valueOf(cnyValue);
			}
		} catch (Exception e) {
			result = 6.33;
			logger.error("发生异常getFromUsdToCny():"+e.getMessage());
		}
		return result;
	}
	
    
	/**
	 * vpp/eth（50000）*eth开盘价*6.33(USD:CNY)
	 */
	@Override
	public Double getVppValue(BigDecimal vpp) {
		try {
			BigDecimal eth = new BigDecimal(50000);
			BigDecimal balance = DealUtil.priceDivide(vpp,eth); // eth:vpp  1:50000
			Double  ethOpenValue = getEthOpenValue(); 
			BigDecimal value = new BigDecimal(0);
			Double rate = getFromUsdToCny();
			Double result = 0D;
			if(ethOpenValue!=null){
				value = DealUtil.priceMultiply(balance,new BigDecimal(ethOpenValue.toString())); // 取出开盘价
			}
			result=DealUtil.priceMultiply(value,new BigDecimal(rate.toString())).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			logger.info("vpp："+vpp+"/eth："+eth+"*eth开盘价："+ethOpenValue+"*汇率："+rate+"约等于人民币："+result);
			return result;
		} catch (Exception e) {
			logger.error("发生异常 getVppValue()："+e.getMessage());
		}
		return null;
	}

	@Override
	public Customer getCustomerByAccount(String account) {
		return customerMapper.selectByAddress(account);
	}

	@Override
	public int updatePayPassword(Map<String, Object> map) {
		return customerMapper.updatePayPassword(map);
	}

}
