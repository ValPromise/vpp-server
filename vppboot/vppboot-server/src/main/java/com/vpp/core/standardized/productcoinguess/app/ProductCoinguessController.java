package com.vpp.core.standardized.productcoinguess.app;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.coinguess.bean.Coinguess;
import com.vpp.core.common.CommonController;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguessList;
import com.vpp.core.standardized.productcoinguess.service.IProductCoinguessService;
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

    @RequestMapping(value = "/getList")
    public ResultVo getProductCoinguessList(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        List<ProductCoinguessList> list = productCoinguessService.selectProductCoinguessByStatus();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("help","合约开始前下单，选择合约开始至到期时，价格涨跌，如果选择正确则得到购买下单金额180%补偿。");
        result.put("product",list);
        return ResultVo.setResultSuccess(result);
    }

    @RequestMapping(value = "/getDetail")
    public ResultVo getProductCoinguessDetail(String productId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        return ResultVo.setResultSuccess(productCoinguessService.selectByProductId(productId));
    }


}
