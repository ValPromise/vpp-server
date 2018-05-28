package com.vpp.core.customer;

import java.math.BigDecimal;
import java.util.Map;

public interface ICustomerService {
	/**
	 * 注册会员
	 * @param customer
	 * @return
	 */
	int  register(Customer customer);
	/**
	 * 查看会员详情信息
	 * @param id
	 * @return
	 */
	Customer selectCustomerById(Long id);
	
	/**
	 * 修改会员信息
	 * @param customer
	 * @return
	 */
	int updateCustomer(Customer customer);
	
	/**
	 * 根据用户名查询会员信息
	 * @param userName
	 * @return
	 */
	Customer selectCustomerByUserName(String userName);
	
	/**
	 * 更新会员账户余额
	 * TODO 添加方法注释
	 * @author cgp
	 * @param customer
	 * @return
	 */
	int updateCustomerBalance(Long id,BigDecimal addBalance);
	
	/**
	 * 根据邀请码查询客户信息
	 * @param invitationCode
	 * @return
	 */
	Customer selectByCode(String invitationCode);
	/**
	 * 查看内部钱包地址
	 * @param id
	 * @return
	 */
	String getWalletAddress(Long id);
	
	/**
	 * vpp兑人民币价格接口
	 * @param vpp
	 * @return
	 */
	Double getVppValue(BigDecimal vpp);
	
	/**
	 * 根据钱包地址查询客户信息
	 * @param account
	 * @return
	 */
	Customer getCustomerByAccount(String account);
	
	/**
	 * 修改支付密码
	 * @param map
	 * @return
	 */
	int updatePayPassword(Map<String, Object> map);
	
}
