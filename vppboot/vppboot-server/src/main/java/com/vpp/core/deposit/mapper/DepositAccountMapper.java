package com.vpp.core.deposit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.vpp.core.deposit.bean.DepositAccount;

@Mapper
public interface DepositAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DepositAccount record);

    int insertSelective(DepositAccount record);

    DepositAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DepositAccount record);

    int updateByPrimaryKey(DepositAccount record);

    /**
     * 根据钱包地址查询详情
     * 
     * @author Lxl
     * @param account
     * @return
     */
    DepositAccount findByAccount(@Param("account") String account);

    int updateBalanceByAccount(@Param("account") String account, @Param("balance") Integer balance);
}