package com.vpp.core.standardized.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vpp.core.customer.Customer;
import com.vpp.core.customer.ICustomerService;
import com.vpp.core.standardized.order.IOrderService;
import com.vpp.core.standardized.order.OrderList;

@Service
public class PayService implements IPayService{

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;
   
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean payCallBack(Customer customer, OrderList orderList) {
        try {
            customerService.updateCustomer(customer);
            orderService.updateByPrimaryKeySelective(orderList);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean pay(Customer customer, OrderList orderList) {
        // TODO Auto-generated method stub
        return false;
    }

}
