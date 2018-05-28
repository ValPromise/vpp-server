package com.vpp.core.standardized.payout;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.page.PageInfo;
import com.vpp.core.customer.CustomerMapper;
import com.vpp.core.customer.ICustomerService;
import com.vpp.core.standardized.order.IOrderService;
import com.vpp.core.standardized.order.OrderListMapper;

@Service
public class OrderPayoutService implements IOrderPayoutService {
    private static final Logger logger = LogManager.getLogger(OrderPayoutService.class);
    
    @Autowired
    private OrderPayoutMapper orderPayoutMapper;
    @Autowired
    private OrderListMapper orderListMapper;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ICustomerService customerService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void payoutRemitToUser() {
        int currentPage = 1;
        int totalPage = 0;// 总页数
        int pageSize = 200;// 每页行数限制
        do {
            PageHelper.startPage(currentPage, pageSize);
            Page<OrderPayout> list = orderPayoutMapper.selectOrderPayoutBypayoutState((byte) 0);
            // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
            PageInfo<OrderPayout> pageInfo = new PageInfo<>(list);
            if (totalPage == 0) {// 取一次总页数
                totalPage = pageInfo.getPages();
            }
            this.updatePayout(pageInfo.getList());// 触发判断
            currentPage++;
        } while (currentPage <= totalPage);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePayout(List<OrderPayout> list){
        if(!list.isEmpty()){
            for (OrderPayout orderPayout : list) {
                Long customerId = orderPayout.getCustomerId();
                String orderId  = orderPayout.getInnerOrderId();
                BigDecimal payout = orderPayout.getPayoutFee();
                try {
                  //更新账户
                    customerService.updateCustomerBalance(customerId,payout);
                    //更新订单
                    orderService.updatePayoutStateByInnerOrderId(orderId,payout);
                    //更新赔付
                    orderPayout.setPayoutState((byte)1);
                    orderPayout.setRealPayoutFee(payout);
                    orderPayout.setDataState((byte)1);
                    orderPayoutMapper.updateByPrimaryKeySelective(orderPayout);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }
    }

    @Override
    public Page<OrderPayout> getPayOutHistoryCustomerId(Integer currentPage, Integer pageSize, Long customerId) {
        PageHelper.startPage(currentPage, pageSize);
        return orderPayoutMapper.getPayOutHistoryCustomerId(customerId);
    }
}
