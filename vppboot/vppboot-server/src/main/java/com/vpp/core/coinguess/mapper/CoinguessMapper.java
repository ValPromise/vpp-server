package com.vpp.core.coinguess.mapper;

import com.github.pagehelper.Page;
import com.vpp.core.coinguess.bean.Coinguess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface CoinguessMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coinguess record);

    int insertSelective(Coinguess record);

    Coinguess selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coinguess record);

    int updateByPrimaryKey(Coinguess record);

    int selectByCustomerIdAndOrderTs(@Param("customerId")Long customerId, @Param("orderTs")String orderTs);

    /* manual add to select Coinguess records by customer id */
    Page<Coinguess> selectCoinguessInfoByCustomerId(@Param("customerId")Long customerId);

    List<Coinguess> selectCoinguessInfoByStatus(@Param("lotteryTime")String lotteryTime);

    List<Coinguess> selectCoinguessInfoByCustomerIdAndStatus(@Param("customerId")Long customerId, @Param("lotteryTime")String lotteryTime);

    BigDecimal getTotalOrderAmountByLotteryTime(@Param("lotteryTime") String lotteryTime);

    Coinguess selectByOrderId(@Param("orderId") String orderId);

    BigDecimal getTotalProfit(@Param("idList") List<Long> idList);

    Integer getTotalOrders(@Param("customerId")Long customerId, @Param("lotteryTime") String lotteryTime);

    List<Coinguess> selectForRefund(@Param("lotteryTime")String Long);

    /**
     * 根据条件查询订单数
     *
     * @author Lxl
     * @param startGmtCreate
     * @param endGmtCreate
     * @return
     */
    List<Map<String, Object>> findCount(@Param("startGmtCreate") String startGmtCreate,
                                        @Param("endGmtCreate") String endGmtCreate);

    /**
     * 根据条件查询赔付金额
     *
     * @author Lxl
     * @param startGmtCreate
     * @param endGmtCreate
     * @return
     */
    List<Map<String, Object>> findPayout(@Param("startGmtCreate") String startGmtCreate,
                                         @Param("endGmtCreate") String endGmtCreate);


}
