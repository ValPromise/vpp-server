package com.vpp.core.standardized.productcoinguess.app;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.coinguess.bean.Coinguess;
import com.vpp.core.common.CommonController;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguessList;
import com.vpp.core.standardized.productcoinguess.service.IProductCoinguessService;
import com.vpp.core.systemparam.bean.SystemParam;
import com.vpp.core.systemparam.service.ISystemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
@RequestMapping("/app/product_coinguess")
public class ProductCoinguessController extends CommonController {
    @Autowired
    private IProductCoinguessService productCoinguessService;

    @Autowired
    private ISystemParamService systemParamService;

    @RequestMapping(value = "/getList")
    public ResultVo getProductCoinguessList(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        List<ProductCoinguessList> list = productCoinguessService.selectProductCoinguessByStatus();

        //读system参数
        SystemParam systemParamHelp = systemParamService.selectByParamTypeAndName("coinguess","coinguess_help");
        SystemParam systemParamAd = systemParamService.selectByParamTypeAndName("coinguess","coinguess_ad");

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("product",list);
        result.put("help",systemParamHelp.getParamValue());
        result.put("ad",systemParamAd.getParamValue());
        return ResultVo.setResultSuccess(result);
    }

    @RequestMapping(value = "/getDetail")
    public ResultVo getProductCoinguessDetail(String productId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        return ResultVo.setResultSuccess(productCoinguessService.selectByProductId(productId));
    }


}
