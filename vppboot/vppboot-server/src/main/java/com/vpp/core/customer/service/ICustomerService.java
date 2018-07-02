package com.vpp.core.customer.service;

import java.math.BigDecimal;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.deposit.bean.DepositAccount;

public interface ICustomerService {
    /**
     * 注册会员
     * 
     * @param customer
     * @return
     */
    int register(Customer customer, DepositAccount depositAccount, Customer inviteCustomer) throws Exception;

    /**
     * 查看会员详情信息
     * 
     * @param id
     * @return
     */
    Customer selectCustomerById(Long id);

    /**
     * 修改会员信息
     * 
     * @param customer
     * @return
     */
    int updateCustomer(Customer customer);

    /**
     * 根据用户名查询会员信息
     * 
     * @param userName
     * @return
     */
    Customer selectCustomerByMobile(String userName);

    /**
     * 收入资金，修改用户余额，添加资金流水
     * 
     * @author Lxl
     * @param id 客户ID
     * @param income 累加余额
     * @param desc 资金描述，用于资金流水
     * @return
     */
    int incomeBalance(Long id, BigDecimal income, String desc);

    /**
     * 支出资金，修改客户余额，添加资金流水
     * 
     * @author Lxl
     * @param id
     * @param expenditure
     * @param desc
     * @return
     */
    int expenditureBalance(Long id, BigDecimal expenditure, String desc) throws Exception;

    /**
     * 根据邀请码查询客户信息
     * 
     * @param invitationCode
     * @return
     */
    Customer selectByCode(String invitationCode);

    /**
     * 查看内部钱包地址
     * 
     * @param id
     * @return
     */
    String getWalletAddress(Long id);

    /**
     * vpp兑人民币价格接口
     * 
     * @param vpp
     * @return
     */
    Double getVppValue(BigDecimal vpp);

    /**
     * 缓存ETH实时美元价格
     * 
     * @author Lxl
     */
    void cacheEthUsdt() throws Exception;

    /**
     * 缓存美元汇率
     * 
     * @author Lxl
     * @throws Exception
     */
    void cacheUsdToCny() throws Exception;

    /**
     * 根据钱包地址查询客户信息
     * 
     * @param account
     * @return
     */
    Customer getCustomerByAccount(String account);

    /**
     * 修改支付密码
     * 
     * @author Lxl
     * @param id
     * @param payPassword
     * @return
     */
    int updatePayPassword(Long id, String payPassword);

    /**
     * 修改密码
     * 
     * @author Lxl
     * @param id
     * @param password
     * @return
     */
    int updatePassword(Long id, String password);

    /**
     * 修改手机号码
     * 
     * @author Lxl
     * @param id
     * @param mobile
     * @return
     */
    int updateMobile(Long id, String mobile);

    /**
     * 统计邀请码使用个数
     * 
     * @param inviteCode
     * @return
     */
    int countInviteCode(String inviteCode);

    /**
     * 根据手机号码查询客户信息
     * 
     * @author Lxl
     * @param mobile
     * @return
     * @throws Exception
     */
    Customer findByMobile(String mobile) throws Exception;

    /**
     * 分页查询
     * 
     * @author Lxl
     * @param currentPage
     * @param pageSize
     * @param params
     * @return
     * @throws Exception
     */
    Page<Customer> findListByCondition(Integer currentPage, Integer pageSize, Map<String, Object> params) throws Exception;

    /**
     * 根据注册时间查询用户数
     * 
     * @author Lxl
     * @param startGmtCreate
     * @param endGmtCreate
     * @return
     */
    Integer findCount(String startGmtCreate, String endGmtCreate);

}
