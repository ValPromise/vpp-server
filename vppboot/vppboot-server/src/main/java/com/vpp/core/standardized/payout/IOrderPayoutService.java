package com.vpp.core.standardized.payout;

import java.util.Map;

import com.github.pagehelper.Page;

public interface IOrderPayoutService {

//    void payoutRemitToUser();

    Page<OrderPayout> getPayOutHistoryCustomerId(Integer currentPage, Integer pageSize, Long customerId);

    /**
     * 分页查询
     * 
     * @author Lxl
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<OrderPayout> findLimit(Integer currentPage, Integer pageSize, Map<String, Object> params);
}
