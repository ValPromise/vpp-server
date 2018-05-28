package com.vpp.core.standardized.pay;

import com.vpp.core.customer.bean.Customer;
import com.vpp.core.standardized.order.bean.OrderList;

public interface IPayService {
    
    boolean payCallBack(Customer customer,OrderList orderList);
    
    boolean pay(Customer customer,OrderList orderList);
}
