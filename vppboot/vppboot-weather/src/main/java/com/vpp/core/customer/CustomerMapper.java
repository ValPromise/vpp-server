package com.vpp.core.customer;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerMapper {
	
	int deleteByPrimaryKey(Long id);

	int insert(Customer record);

	int insertSelective(Customer record);

	Customer selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Customer record);

	int updateByPrimaryKey(Customer record);
	
	Customer selectCustomerByUserName(@Param("userName")String userName);
	
	int updateCustomerBalance(@Param("customerId")Long id,@Param("addBalance")BigDecimal addBalance);

	Customer selectByCode(@Param("invitationCode")String invitationCode);
	
	String getWalletAddress(@Param("customerId")Long id);
	
	Customer selectByAddress(@Param("address") String address);
	
	int updatePayPassword(Map<String, Object> map);
	
}