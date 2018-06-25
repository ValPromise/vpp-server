package com.vpp.core.systemparam.mapper;

import com.vpp.core.systemparam.bean.SystemParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemParamMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemParam record);

    int insertSelective(SystemParam record);

    SystemParam selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemParam record);

    int updateByPrimaryKey(SystemParam record);

    SystemParam selectByParamName(@Param("paramName")String paramName);

    SystemParam selectByParamTypeAndName(@Param("paramType")String paramType, @Param("paramName")String paramName);
}