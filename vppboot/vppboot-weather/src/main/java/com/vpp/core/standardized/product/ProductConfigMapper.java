package com.vpp.core.standardized.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductConfig record);

    int insertSelective(ProductConfig record);

    ProductConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductConfig record);

    int updateByPrimaryKey(ProductConfig record);
    
    ProductConfig selectByProductId(@Param("productId")String productId);
    
}