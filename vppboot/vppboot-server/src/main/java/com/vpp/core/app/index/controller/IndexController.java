package com.vpp.core.app.index.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.AppUtils;
import com.vpp.common.utils.ConstantsOrder;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.vo.AppOrderListVo;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.banner.bean.Banner;
import com.vpp.core.banner.service.IBannerService;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.notice.bean.Notice;
import com.vpp.core.notice.service.INoticeService;
import com.vpp.core.standardized.order.service.IOrderService;

@RestController
@RequestMapping("/app")
public class IndexController extends CommonController {

    private static final Logger logger = LogManager.getLogger(IndexController.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IBannerService bannerService;
    @Autowired
    private INoticeService noticeService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;

    @RequestMapping("/index")
    public ResultVo index(String mobile, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Integer currentPage = 1;
        Integer pageSize = 20;
        try {
            List<Map<String, Object>> banners = this.getBannerList(currentPage, pageSize);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("state", ConstantsServer.STATE_ENABLE);
            List<Notice> notices = noticeService.findLimit(1, 2, params);
            Customer customer = customerService.findByMobile(mobile);
            List<OrderInfoVo> tempOrders = orderService.getSuccessOrdersByCondition(1, 2, customer.getId(),
                    ConstantsOrder.Product.PRODUCT_ID_TEMP);
            List<OrderInfoVo> rainOrders = orderService.getSuccessOrdersByCondition(1, 2, customer.getId(),
                    ConstantsOrder.Product.PRODUCT_ID_RAIN);

            List<AppOrderListVo> tempAppOrders = AppUtils.getAppVoList(tempOrders);
            List<AppOrderListVo> rainAppOrders = AppUtils.getAppVoList(rainOrders);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("banners", banners);
            map.put("notices", notices);
            map.put("tempOrders", tempAppOrders);
            map.put("rainOrders", rainAppOrders);
            return ResultVo.setResultSuccess(map);
        } catch (Exception e) {
        }

        return ResultVo.setResultSuccess();
    }

    private List<Map<String, Object>> getBannerList(Integer currentPage, Integer pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", ConstantsServer.STATE_ENABLE);
        Page<Banner> list = bannerService.getBannerList(currentPage, pageSize, params);
        PageInfo<Banner> pageInfos = new PageInfo<Banner>(list);
        List<Map<String, Object>> banners = new ArrayList<Map<String, Object>>();
        for (Banner banner : pageInfos.getList()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("linkUrl", banner.getLinkurl());
            map.put("imgUrl", banner.getImgurl());
            banners.add(map);
        }
        return banners;
    }

}
