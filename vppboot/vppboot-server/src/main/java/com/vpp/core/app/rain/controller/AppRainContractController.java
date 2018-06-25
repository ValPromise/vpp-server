package com.vpp.core.app.rain.controller;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.standardized.order.bean.DataModelDto;
import com.vpp.core.standardized.order.service.IContractService;
import com.vpp.service.city.bean.CityInfo;
import com.vpp.service.city.service.ICityService;

/**
 * app降雨合约查询
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
@RestController
@RequestMapping("/app/rain/contract")
public class AppRainContractController extends CommonController {
    @Autowired
    private IContractService contractService;
    @Autowired
    private ICityService cityService;

    /**
     * 根据城市ID查询合约
     * 
     * @author Lxl
     * @param token
     * @param cityId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectContract")
    @ResponseBody
    public ResultVo selectContract(String token, String cityId, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        // if (!checkLogin(token)) {
        // return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        // }

        // 默认保障 第二天
        Date tomorrow = DateUtil.addDate(new Date(), 1);
        String customerId = getCustomerIdByToken(token);
        // 查询当前
        String startTime = DateUtil.format(tomorrow, DateUtil.YMD_DATE_TIME_PATTERN);
        String endTime = DateUtil.format(DateUtil.addDate(tomorrow, 1), DateUtil.YMD_DATE_TIME_PATTERN);
        DataModelDto dataModelDto = contractService.selectTemplateId108(cityId, startTime, endTime, customerId);
        if (null == dataModelDto) {
            return ResultVo.setResultError();
        }

        CityInfo city = cityService.findCityInfoByCityId(dataModelDto.getCityId());
        String explainFirst = getMessage("rain_explain_first");
        String explainSecond = MessageFormat.format(getMessage("rain_explain_second"), city.getCnName(),
                city.getCityId().substring(2));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("explainFirst", explainFirst);
        result.put("explainSecond", explainSecond);
        result.put("contractTime", dataModelDto.getStime());
        result.put("contractId", dataModelDto.getInnerOrderId());
        result.put("payout", dataModelDto.getPayoutFee());
        result.put("price", dataModelDto.getPrice());
        result.put("threshold", dataModelDto.getThreshold());
        result.put("maxBuyCount", ConstantsRain.MAX_BUY_COUNT);// 最大购买份数
        return ResultVo.setResultSuccess(result);
    }
}
