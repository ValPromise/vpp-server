package com.vpp.core.standardized.productcoinguess.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguessList;
import com.vpp.core.standardized.productcoinguess.mapper.ProductCoinguessMapper;
import com.vpp.core.systemparam.bean.SystemParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vpp.core.systemparam.service.ISystemParamService;

@Service
public class ProductCoinguessService implements IProductCoinguessService {
    @Autowired
    private ProductCoinguessMapper productCoinguessMapper;

    @Autowired
    private ISystemParamService systemParamService;

    @Override
    public List<ProductCoinguessList> selectProductCoinguessByStatus(){

        List<ProductCoinguess> fromBeanLists = productCoinguessMapper.selectProductCoinguessByStatus();
        List<ProductCoinguessList> toBeanList = new ArrayList<ProductCoinguessList>();

        //对ProductCoinguess字段进行过滤，映射为ProductCoinguessList输出
        for (int i = 0; i < fromBeanLists.size(); i++) {
            ProductCoinguessList tmp = new ProductCoinguessList();
            BeanUtils.copyProperties(fromBeanLists.get(i), tmp);
            toBeanList.add(tmp);
        }

        //读system参数，看是否对货币对随机排序 Y-随机，N-不随机
        SystemParam systemParam = systemParamService.selectByParamTypeAndName("coinguess","random_order");

        //如果要随机化，对toBeanList进行洗牌（随机化）
        if (systemParam == null)
            return toBeanList;
        else{

            if("Y".equals(systemParam.getParamValue())){
                Collections.shuffle(toBeanList);
            }
            return toBeanList;
        }

    }

    @Override
    public ProductCoinguess selectByProductId(String productId){
        return productCoinguessMapper.selectByProductId(productId);
    }

    @Override
    public List<String> selectProductId(){
        return productCoinguessMapper.selectProductId();
    }

    @Override
    public List<ProductCoinguess> selectProductCoinguessByStatusFull(){
        return productCoinguessMapper.selectProductCoinguessByStatus();
    }

}
