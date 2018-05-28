package com.vpp.core.standardized.product;

import org.apache.ibatis.annotations.Param;

public interface IProductConfigService {
    
    ProductConfig selectProductByProductId(@Param("productId")String productId);
    
    String getProductNameByProductId(String productId);
}
