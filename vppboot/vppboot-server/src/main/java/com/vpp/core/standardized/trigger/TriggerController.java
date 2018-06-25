package com.vpp.core.standardized.trigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.core.rain.service.IRainOrderTriggerService;
import com.vpp.core.standardized.payout.IOrderPayoutService;

/**
 * 触发定时任务接口
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
@RestController
@RequestMapping("/trigger")
public class TriggerController {
    @Autowired
    private ITriggerService triggerService;
    @Autowired
    private IRainOrderTriggerService rainOrderTriggerService;
    @Autowired
    private IOrderPayoutService orderPayoutService;

    // @RequestMapping(value = "/triggerByEtime")
    // public ResultVo updateTriggerByDate(String sign, HttpServletResponse response, HttpServletRequest request) {
    // String etime = DateUtil.getYesterdayString(DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN));
    // try {
    // // 触发高温订单数据
    // triggerService.triggerByEtime(etime);
    // // 触发降雨订单数据
    // rainOrderTriggerService.triggerByEtime(etime);
    // } catch (Exception e) {
    // return ResultVo.setResultError(e.getMessage());
    // }
    // return ResultVo.setResultSuccess("ok");
    // }
    //
    // @RequestMapping(value = "/payout")
    // public ResultVo payout(HttpServletResponse response, HttpServletRequest request) {
    // try {
    // orderPayoutService.payoutRemitToUser();
    // } catch (Exception e) {
    // return ResultVo.setResultError(e.getMessage());
    // }
    // return ResultVo.setResultSuccess();
    //
    // }
}
