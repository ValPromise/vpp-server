package com.vpp.core.standardized.productcoinguess.service;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguessList;

import java.util.List;

public interface IProductCoinguessService {
    List<ProductCoinguessList>  selectProductCoinguessByStatus();
    ProductCoinguess selectByProductId(String productId);
    List<String> selectProductId();
    List<ProductCoinguess> selectProductCoinguessByStatusFull();
}
