package com.vpp.core.coinguess.mapper;

import com.github.pagehelper.Page;
import com.vpp.core.coinguess.bean.Coinguess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CoinguessMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coinguess record);

    int insertSelective(Coinguess record);

    Coinguess selectByPrimaryKey(Long id);

    int selectByCustomerIdAndOrderTs(@Param("customerId")Long customerId, @Param("orderTs")String orderTs);

    int updateByPrimaryKeySelective(Coinguess record);

    int updateByPrimaryKey(Coinguess record);

    /* manual add to select Coinguess records by customer id */
    Page<Coinguess> selectCoinguessInfoByCustomerId(@Param("customerId")Long customerId);

    List<Coinguess> selectCoinguessInfoByStatus(@Param("lotteryTime")String lotteryTime);

    List<Coinguess> selectCoinguessInfoByCustomerIdAndStatus(@Param("customerId")Long customerId, @Param("lotteryTime")String lotteryTime);

    BigDecimal getTotalOrderAmountByLotteryTime(@Param("lotteryTime") String lotteryTime);

    Coinguess selectByOrderId(@Param("orderId") String orderId);

    BigDecimal getTotalProfit(@Param("idList") List<Long> idList);

    Integer getTotalOrders(@Param("customerId")Long customerId, @Param("lotteryTime") String lotteryTime);


}