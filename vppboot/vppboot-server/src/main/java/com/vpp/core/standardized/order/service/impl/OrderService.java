package com.vpp.core.standardized.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.OrderVo;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;
import com.vpp.core.standardized.order.mapper.OrderCityMapper;
import com.vpp.core.standardized.order.mapper.OrderListMapper;
import com.vpp.core.standardized.order.service.IOrderService;
import com.vpp.core.standardized.product.IProductConfigService;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderListMapper orderListMapper;
    @Autowired
    private OrderCityMapper orderCityMapper;
    @Autowired
    private IProductConfigService productConfigService;
    @Autowired
    private CustomerMapper customerMapper;

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
    public Page<OrderInfoVo> getSuccessOrdersByCondition(Integer currentPage, Integer pageSize, Long customerId,
            String productId) {
        PageHelper.startPage(currentPage, pageSize);
        return orderListMapper.getSuccessOrdersByCondition(customerId, productId);
    }

    @Override
    public OrderInfoVo findOrderInfoVoByInnerOrderId(String innerOrderId) throws Exception {
        return orderListMapper.findOrderInfoVoByInnerOrderId(innerOrderId);
    }

    public static final String SALEINFO_VPP_PRE = "saleinfo_vpp_";
    @Autowired
    private RedisTemplate<String, String> buyRedis;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tempContractPay(OrderList orderList, OrderCity orderCity, Customer customer) throws Exception {
        orderListMapper.insertSelective(orderList);
        orderCityMapper.insertSelective(orderCity);

        // String aaa = "asdfasd";
        // Long a = Long.valueOf(aaa);
        // customer.setNickName(
        // "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        // 事物回滚
        customerMapper.updateByPrimaryKeySelective(customer);

        String time = DateUtil.format(orderList.getStime(), DateUtil.YMD_DATE_PATTERN);// 2018-05-27
        // 数据回写入redis做风控
        String saleCacheKey = SALEINFO_VPP_PRE + orderCity.getCityId() + "_"
                + DateUtil.strToStr(time, DateUtil.YMD_DATE_PATTERN, DateUtil.YMD_DATE_TIME_PATTERN);
        String saleCntStr = (String) buyRedis.opsForHash().get(saleCacheKey, orderCity.getThreshold().toString());
        int saleCnt = StringUtils.isBlank(saleCntStr) ? 0 : Integer.valueOf(saleCntStr);
        buyRedis.opsForHash().put(saleCacheKey, orderCity.getThreshold().toString(),
                String.valueOf(saleCnt + orderList.getBuyCount()));

    }
}
