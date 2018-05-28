package com.vpp.core.standardized.payout;

import com.github.pagehelper.Page;

public interface IOrderPayoutService {
    
    void payoutRemitToUser();

    Page<OrderPayout> getPayOutHistoryCustomerId(Integer currentPage, Integer pageSize, Long customerId);
}
