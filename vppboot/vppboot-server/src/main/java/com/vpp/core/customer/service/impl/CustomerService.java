package com.vpp.core.customer.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.StringUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.common.EthController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.deposit.bean.DepositAccount;
import com.vpp.core.deposit.mapper.DepositAccountMapper;

@Service
public class CustomerService implements ICustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    /** 汇率URL **/
    private final String FROM_USD_TO_CNY_URL = "http://119.28.70.201:9961/chinaNum";

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private DepositAccountMapper depositAccountMapper;
    @Autowired
    private ICustomerCashLogService customerCashLogService;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisTemplate<String, String> customerServiceRedis;

    public static final String ETH_OPEN_VALUE_KEY = "eth_open_value_key";
    public static final int ETH_OPEN_VALUE_KEY_TIME = 10; // 10秒
    public static final String FROM_USD_TO_CNY_KEY = "from_usd_to_cny_key";
    public static final int FROM_USD_TO_CNY_TIME = 60 * 60; // 1小时

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int register(Customer customer, DepositAccount depositAccount, Customer inviteCustomer) throws Exception {
        BigDecimal registerAmount = new BigDecimal(ConstantsServer.REGISTER_CUSTOMER_ADD_VPP);
        customer.setBalance(registerAmount);// 赠送30VPP
        int re = customerMapper.insertSelective(customer);
        depositAccount.setCustomerId(customer.getId());// 同步客户ID
        depositAccountMapper.insertSelective(depositAccount);

        // 资金流水记录注册赠送VPP
        customerCashLogService.insertRegister(customer.getId(), registerAmount, "注册赠送");
        if (null != inviteCustomer) {
            Customer newInviteCustomer = new Customer();
            newInviteCustomer.setId(inviteCustomer.getId());
            BigDecimal inviteAmount = new BigDecimal(ConstantsServer.INVITE_CUSTOMER_ADD_VPP);
            BigDecimal newBalance = DealUtil.priceAdd(inviteCustomer.getBalance(), inviteAmount);
            newInviteCustomer.setBalance(newBalance);
            customerMapper.updateByPrimaryKeySelective(newInviteCustomer);
            customerCashLogService.insertInvite(inviteCustomer.getId(), inviteAmount, "邀请赠送");
        }
        return re;
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
    public Customer selectCustomerByMobile(String userName) {
        return customerMapper.selectCustomerByMobile(userName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int incomeBalance(Long id, BigDecimal income, String desc) {
        customerCashLogService.income(id, income, desc);
        return customerMapper.incomeBalance(id, income);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int expenditureBalance(Long id, BigDecimal expenditure, String desc) throws Exception {
        customerCashLogService.expenditure(id, expenditure, desc);
        return customerMapper.expenditureBalance(id, expenditure);
    }

    @Override
    public Customer selectByCode(String invitationCode) {
        return customerMapper.selectByCode(invitationCode);
    }

    @Override
    public String getWalletAddress(Long id) {
        return customerMapper.getWalletAddress(id);
    }

    @Override
    public void cacheEthUsdt() throws Exception {
        try {
            ResultVo resultVo = EthController.queryPoloniexEthTicker();
            if (resultVo != null) {
                Double askAmount = Double.valueOf(resultVo.getData().toString());
                if (askAmount != null) {
                    customerServiceRedis.opsForValue().set(ETH_OPEN_VALUE_KEY, askAmount.toString(),
                            ETH_OPEN_VALUE_KEY_TIME * 60, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error("获取ETH行情出错  ::: {}", EthController.HUOBI_ETH_USDT_URL);
        }
    }

    @Override
    public void cacheUsdToCny() throws Exception {
        String data = HttpUtils.get(FROM_USD_TO_CNY_URL, new HashMap<String, String>());
        Map<String, Object> map = gson.fromJson(data, new TypeToken<Map<String, Object>>() {
        }.getType());
        Double value = Double.valueOf(map.get("result").toString());
        Double result = value / 100;
        customerServiceRedis.opsForValue().set(FROM_USD_TO_CNY_KEY, result.toString(), FROM_USD_TO_CNY_TIME + 24,
                TimeUnit.SECONDS);
    }

    /**
     * 美元 ->人民币
     * 
     * @return
     */
    public Double getFromUsdToCny() {
        String cnyValue = customerServiceRedis.opsForValue().get(FROM_USD_TO_CNY_KEY);
        if (StringUtils.isEmpty(cnyValue)) {
            return 6.40d;
        } else {
            return Double.valueOf(cnyValue);
        }
    }

    /**
     * vpp/eth（50000）*eth开盘价*6.33(USD:CNY)
     */
    @Override
    public Double getVppValue(BigDecimal vpp) {
        try {
            BigDecimal eth = new BigDecimal(50000);
            BigDecimal balance = DealUtil.priceDivide(vpp, eth); // eth:vpp 1:50000
            Double ethOpenValue = getEthOpenValue();
            BigDecimal value = new BigDecimal(0);
            Double rate = getFromUsdToCny();
            Double result = 0D;
            if (ethOpenValue != null) {
                value = DealUtil.priceMultiply(balance, new BigDecimal(ethOpenValue.toString())); // 取出开盘价
            }
            result = DealUtil.priceMultiply(value, new BigDecimal(rate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            // logger.info("vpp：" + vpp + "/eth：" + eth + "*eth开盘价：" + ethOpenValue + "*汇率：" + rate + "约等于人民币：" + result);
            return result;
        } catch (Exception e) {
            logger.error("发生异常 getVppValue()：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取eth开盘价
     * 
     * @return 开盘价
     */
    private Double getEthOpenValue() {
        Double openValue = 0D;
        try {
            String ethOpenValue = customerServiceRedis.opsForValue().get(ETH_OPEN_VALUE_KEY);
            if (StringUtils.isEmpty(ethOpenValue)) {
                this.cacheEthUsdt();
                ethOpenValue = customerServiceRedis.opsForValue().get(ETH_OPEN_VALUE_KEY);
            } else {
                openValue = Double.valueOf(ethOpenValue);
            }
        } catch (Exception e) {
            logger.error("发生异常 getVppOpenValue() : {}" + e.getMessage());
        }
        return openValue;
    }

    @Override
    public Customer getCustomerByAccount(String account) {
        return customerMapper.selectByAddress(account);
    }

    @Override
    public int updatePayPassword(Long id, String payPassword) {
        return customerMapper.updatePayPassword(id, payPassword);
    }

    @Override
    public int countInviteCode(String inviteCode) {
        return customerMapper.countInviteCode(inviteCode);
    }

    @Override
    public Customer findByMobile(String mobile) throws Exception {
        return customerMapper.findByMobile(mobile);
    }

    @Override
    public int updatePassword(Long id, String password) {
        return customerMapper.updatePassword(id, password);
    }

    @Override
    public int updateMobile(Long id, String mobile) {
        return customerMapper.updateMobile(id, mobile);
    }

    @Override
    public Page<Customer> findListByCondition(Integer currentPage, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(currentPage, pageSize);
        return customerMapper.findListByCondition(params);
    }
}
