package com.vpp.core.coinguess.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.coinguess.bean.Coinguess;

public interface ICoinguessService {
    /**
     * 新增猜币价订单
     * 
     * @param coinguess
     * @return 1-插入成功; 0-插入失败
     */
    int insertCoinguessOrder(Coinguess coinguess, Long customerId, Long ownerId, BigDecimal orderAmt);

    /**
     * 根据主键查询币价订单
     * 
     * @param id
     * @return Coinguess对象
     */
    Coinguess selectCoinguessOrderById(Long id);

    /**
     * 根据orderId查询币价订单
     * 
     * @param orderId
     * @return Coinguess对象
     */
    Coinguess selectCoinguessOrderByOrderId(String orderId);

    /**
     * 根据用户id和投注时间戳查找记录是否存在
     * 
     * @param customerId
     * @param orderTs
     * @return true - 存在，false - 不存在
     */
    int checkOrderExistence(Long customerId, String orderTs);

    /**
     * 更新猜币价订单
     * 
     * @param coninguess
     * @return 1-更新成功; 0-更新失败
     */
    int updateCoinguessOrder(Coinguess coninguess);

    /**
     * 根据客户id查猜币订单列表
     * 
     * @param pageNum
     * @param pageSize
     * @param customerId
     * @return
     */
    Page<Coinguess> selectCoinguessInfoByCustomerId(int pageNum, int pageSize, Long customerId);

    /**
     * 根据开奖时间查询待开奖的数据list
     * 
     * @return list<Coinguess>
     */
    List<Coinguess> selectCoinguessInfoByStatus(String lotteryTime);

    /**
     * 通过客户号和,开奖时间和开奖状态筛选该客户未开奖的数据
     *
     * @param customerId
     * @return List<Coinguess>
     */
    List<Coinguess> selectCoinguessInfoByCustomerIdAndStatus(Long customerId, String lotteryTime);

    /**
     * 获得多个订单总收益
     *
     * @param idList
     * @return 订单总金额
     */
    BigDecimal getTotalProfit(List<Long> idList);

    /**
     * 批量更新下单价格（根据开奖时间）
     * @param long lotteryTime开奖时间
     */
    void batchUpdateOrderPrice(Long lotteryTime);

    /**
     * 开奖方法（开单个客户的奖），将该客户在特定开奖时间，未开奖的数据筛选出来开奖
     *
     * @param customerId
     * @param lotteryTime
     * @return  BigDecimal
     */
    BigDecimal runTheLottery(Long customerId, String lotteryTime);

    /**
     * 前端未发开奖请求时，系统批量开奖
     *
     * @param currentTimestamp
     */
    void batchLottery(Long currentTimestamp);

    /**
     * 获得同一开奖时间的订单总金额
     * 
     * @param lotteryTime
     * @return 订单总金额
     */
    BigDecimal getTotalOrderAmountByLotteryTime(String lotteryTime);

    /**
     * 获得同一客户在同一期的下单数量
     *
     * @param customerId
     * @param lotteryTime
     * @return 下单数量
     */
    Integer getTotalOrders(Long customerId, String lotteryTime);

    /**
     * 批量退款（5分钟内无法开奖则退款）
     *
     * @return
     */
    void batchRefund();

}
