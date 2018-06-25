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
import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.ConstantsTemp;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.AppOrderListVo;
import com.vpp.common.vo.CoinguessOrderListVo;
import com.vpp.common.vo.OrderInfoVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.banner.bean.Banner;
import com.vpp.core.banner.service.IBannerService;
import com.vpp.core.coinguess.bean.Coinguess;
import com.vpp.core.coinguess.service.ICoinguessService;
import com.vpp.core.common.CommonController;
import com.vpp.core.notice.bean.Notice;
import com.vpp.core.notice.service.INoticeService;
import com.vpp.core.standardized.order.service.IOrderService;

@RestController
@RequestMapping("/app")
public class AppIndexController extends CommonController {

    private static final Logger logger = LogManager.getLogger(AppIndexController.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IBannerService bannerService;
    @Autowired
    private INoticeService noticeService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICoinguessService coinguessService;

    private static final Integer DEFAULT_PAGE = 1;

    private static final Integer DEFAULT_PAGE_SIZE = 2;

    @RequestMapping("/index")
    public ResultVo index(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"));
        }

        Long customerId = this.getCustomerId(token);
        Integer currentPage = 1;
        Integer pageSize = 20;
        try {
            List<Map<String, Object>> banners = this.getBannerList(currentPage, pageSize);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("state", ConstantsServer.STATE_ENABLE);
            List<Notice> notices = noticeService.findLimit(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, params);
            // Customer customer = customerService.findByMobile(mobile);
            List<OrderInfoVo> tempOrders = orderService.getSuccessOrdersByCondition(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, customerId,
                    ConstantsTemp.PRODUCT_ID);
            List<OrderInfoVo> rainOrders = orderService.getSuccessOrdersByCondition(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, customerId,
                    ConstantsRain.PRODUCT_ID);

            List<AppOrderListVo> tempAppOrders = AppUtils.getAppVoList(tempOrders);
            List<AppOrderListVo> rainAppOrders = AppUtils.getAppVoList(rainOrders);

            Page<Coinguess> coinguessList = coinguessService.selectCoinguessInfoByCustomerId(DEFAULT_PAGE, DEFAULT_PAGE_SIZE,
                    customerId);
            List<CoinguessOrderListVo> coinguessOrderListVos = this.putCoinguessVo(coinguessList.getResult());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("banners", banners);
            map.put("notices", notices);
            map.put("tempOrders", tempAppOrders);
            map.put("rainOrders", rainAppOrders);
            map.put("coinguessOrders", coinguessOrderListVos);
            return ResultVo.setResultSuccess(map);
        } catch (Exception e) {
        }

        return ResultVo.setResultSuccess();
    }

    private List<CoinguessOrderListVo> putCoinguessVo(List<Coinguess> csList) {
        List<CoinguessOrderListVo> voList = new ArrayList<>();
        for (Coinguess cc : csList) {
            CoinguessOrderListVo vo = new CoinguessOrderListVo();
            vo.setInnerOrderId(cc.getOrderId());
            vo.setTitle(cc.getTargetId());
            String content = 0 == cc.getOrderDir() ? "看涨" : "看跌";
            vo.setContent(content);
            vo.setGmtCreate(DateUtil.removeS(cc.getGmtCreate()));
            String statusString = "";
            String payout = "0";
            if (0 == cc.getStatus()) {
                statusString = "等待判定";
            } else if (1 == cc.getStatus()) {
                statusString = "已判定，无需履行";
            } else {
                payout = cc.getProfit().toString();
                statusString = "已判定，已履行";
            }
            vo.setAmount(cc.getOrderAmt().toString() + "-" + payout);
            vo.setStatus(statusString);
            voList.add(vo);
        }
        return voList;
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
