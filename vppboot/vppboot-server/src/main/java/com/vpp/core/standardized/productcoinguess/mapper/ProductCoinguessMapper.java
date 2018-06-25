package com.vpp.core.standardized.productcoinguess.mapper;

import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCoinguessMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductCoinguess record);

    int insertSelective(ProductCoinguess record);

    ProductCoinguess selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductCoinguess record);

    int updateByPrimaryKey(ProductCoinguess record);

    ProductCoinguess selectByProductId(@Param("productId") String productId);

    List<ProductCoinguess> selectProductCoinguessByStatus();

    List<String> selectProductId();
}