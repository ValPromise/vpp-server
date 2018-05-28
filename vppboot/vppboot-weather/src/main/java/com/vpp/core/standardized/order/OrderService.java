package com.vpp.core.standardized.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.standardized.product.IProductConfigService;
import com.vpp.vo.OrderInfoVo;
import com.vpp.vo.OrderVo;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderListMapper orderListMapper;
    @Autowired
    private OrderCityMapper orderCityMapper;
    @Autowired
    private IProductConfigService productConfigService;

    @Override
    public Page<OrderVo> getOrderList(Integer currentPage, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(currentPage, pageSize);
        Page<OrderVo> pageOrder = new Page<OrderVo>();
        Page<OrderList> pageList = orderListMapper.getOrderList(params);
        pageOrder.setTotal(pageList.getTotal());
        pageOrder.setPageNum(pageList.getPageNum());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (OrderList order : pageList.toPageInfo().getList()) {
            Map<String, Object> map = new HashMap<String, Object>();
            OrderVo vo = new OrderVo();
            vo.setOrderList(order);
            List<OrderCity> citys = orderCityMapper.getCitysByOrderId(order.getInnerOrderId());
            vo.setOrderCitys(citys);
            pageOrder.add(vo);
        }
        return pageOrder;
    }

    @Override
    public OrderVo getOrderInfo(Map<String, Object> map) {
        if (map.size() > 0) {
            OrderVo vo = new OrderVo();
            OrderList orderList = orderListMapper.getOrderInfoByOrderId(map);
            if (orderList != null) {
                vo.setOrderList(orderList);
                vo.setProductName(productConfigService.getProductNameByProductId(orderList.getProductId()));
                List<OrderCity> citys = orderCityMapper.getCitysByOrderId(orderList.getInnerOrderId());
                vo.setOrderCitys(citys);
                return vo;
            }
        }
        return null;
    }

    @Override
    public OrderList getOrderByOrderId(String innerOrderId) {
        return orderListMapper.getOrderByOrderId(innerOrderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrder(OrderList order, OrderCity city) {
        try {
            orderListMapper.insertSelective(order);
            orderCityMapper.insertSelective(city);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    @Override
    public Page<OrderList> findLimitByTemplateIdEtime(int currentPage, int pageSize, String templateId, String etime) {
        PageHelper.startPage(currentPage, pageSize);
        return orderListMapper.findLimitByTemplateIdEtime(templateId, etime);
    }

    @Override
    public List<OrderCity> getOrderCityByInnerOrderId(String innerOrderId) {
        return orderCityMapper.getCitysByOrderId(innerOrderId);
    }

    @Override
    public int updatePayoutStateByInnerOrderId(String innerOrderId, BigDecimal payoutFee) {
        return orderListMapper.updatePayoutStateByInnerOrderId(innerOrderId, payoutFee);
    }

    @Override
    public int updateByPrimaryKeySelective(OrderList record) {
        return orderListMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Page<OrderList> getPaySuccessOrderByCustomerId(Integer currentPage, Integer pageSize, Long customerId) {
        PageHelper.startPage(currentPage, pageSize);
        return orderListMapper.getPaySuccessOrderByCustomerId(customerId);
    }

    @Override
    public Page<OrderInfoVo> selectOrderList(Integer currentPage, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(currentPage, pageSize);
        return orderListMapper.selectOrderList(params);
    }

    @Override
    public OrderInfoVo selectOrderInfo(Map<String, Object> params) {
        return orderListMapper.selectOrderInfo(params);
    }

    @Override
    public Integer fingRiskCountByCustomerIdCityId(String productId, Long customerId, String cityId, String date)
            throws Exception {
        return orderListMapper.fingRiskCountByCustomerIdCityId(productId, customerId, cityId, date);
    }

    @Override
    public Integer fingRiskCountByProductId(String productId, String cityId, String date) throws Exception {
        return orderListMapper.fingRiskCountByProductId(productId, cityId, date);
    }

    @Override
    public Integer fingRiskCountByCustomerId(String productId, Long customerId, String date) throws Exception {
        return orderListMapper.fingRiskCountByCustomerId(productId, customerId, date);
    }
}
