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
        result.put("help","合约规则：\n1.以60s移动价格均线上所选价格为下单价格，在系统允许次数内可重复下单\n2.合约到期时，系统以来自交易所的币价与用户下单价格对比，如果用户预判正确，则获得下单金额180%补偿\n3.如有疑问，请APP内用户反馈留言，或加微信valpromise-vpp\n注：60s移动价格均线上任意一点的值＝该点之前60s价格平均值");
        result.put("ad","公募认购倒计时！详情添加微信小助理：valpromise-vpp");
        result.put("product",list);
        return ResultVo.setResultSuccess(result);
    }

    @RequestMapping(value = "/getDetail")
    public ResultVo getProductCoinguessDetail(String productId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        return ResultVo.setResultSuccess(productCoinguessService.selectByProductId(productId));
    }


}
