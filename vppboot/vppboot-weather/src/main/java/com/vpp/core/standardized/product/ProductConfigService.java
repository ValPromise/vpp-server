package com.vpp.core.standardized.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductConfigService implements IProductConfigService {

    @Autowired
    private ProductConfigMapper productConfigMapper;

    @Override
    public ProductConfig selectProductByProductId(String productId) {
        return productConfigMapper.selectByProductId(productId);
    }

    @Override
    public String getProductNameByProductId(String productId) {
        String productName = "";
        ProductConfig product = this.selectProductByProductId(productId);
        if(product != null){
            productName = product.getProductName();
        }
        return productName;
    }

}
