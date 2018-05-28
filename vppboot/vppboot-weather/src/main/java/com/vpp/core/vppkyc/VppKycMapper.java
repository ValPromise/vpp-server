package com.vpp.core.vppkyc;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface VppKycMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VppKyc record);

    int insertSelective(VppKyc record);

    VppKyc selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VppKyc record);

    int updateByPrimaryKey(VppKyc record);
}