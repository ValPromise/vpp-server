package com.vpp.core.customer.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.customer.bean.Customer;

@Mapper
public interface CustomerMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    Customer selectCustomerByMobile(@Param("mobile") String mobile);

    int incomeBalance(@Param("customerId") Long id, @Param("income") BigDecimal income);

    int expenditureBalance(@Param("customerId") Long id, @Param("expenditure") BigDecimal expenditure);

    Customer selectByCode(@Param("invitationCode") String invitationCode);

    String getWalletAddress(@Param("customerId") Long id);

    Customer selectByAddress(@Param("address") String address);

    int updatePayPassword(@Param("id") Long id, @Param("payPassword") String payPassword);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateMobile(@Param("id") Long id, @Param("mobile") String mobile);

    int countInviteCode(@Param("inviteCode") String inviteCode);

    Customer findByMobile(@Param("mobile") String mobile);

    Page<Customer> findListByCondition(Map<String, Object> params);
}