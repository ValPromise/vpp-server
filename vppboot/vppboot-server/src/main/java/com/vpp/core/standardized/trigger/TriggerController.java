package com.vpp.core.standardized.trigger;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.standardized.payout.IOrderPayoutService;

@RestController
@RequestMapping("/trigger")
public class TriggerController {
    @Autowired
    private ITriggerService triggerService;
    @Autowired
    private IOrderPayoutService orderPayoutService;

    @RequestMapping(value = "/triggerByEtime")
    public ResultVo updateTriggerByDate(String sign, HttpServletResponse response, HttpServletRequest request) {
        String etime = DateUtil.getYesterdayString(DateUtil.format(new Date(), DateUtil.YMD_DATE_PATTERN));
        try {
            triggerService.triggerByEtime(etime);
        } catch (Exception e) {
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess("ok");
    }

    @RequestMapping(value = "/payout")
    public ResultVo payout(HttpServletResponse response, HttpServletRequest request) {
        try {
            orderPayoutService.payoutRemitToUser();
        } catch (Exception e) {
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();

    }
}
