package com.vpp.common.vo;

import java.io.Serializable;
import java.util.List;

import com.vpp.core.standardized.order.bean.OrderCity;
import com.vpp.core.standardized.order.bean.OrderList;

public class OrderVo implements Serializable{

    /**
     * TODO 添加字段注释
     */
    private static final long serialVersionUID = 5106726338248760258L;
    
    private OrderList orderList;
    
    private List<OrderCity> orderCitys;
    
    private String productName;
    
    public OrderList getOrderList() {
        return orderList;
    }

    public void setOrderList(OrderList orderList) {
        this.orderList = orderList;
    }

    public List<OrderCity> getOrderCitys() {
        return orderCitys;
    }

    public void setOrderCitys(List<OrderCity> orderCitys) {
        this.orderCitys = orderCitys;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    
}
