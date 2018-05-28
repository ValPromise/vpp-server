package com.vpp.core.vithdrawal;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VithdrawalMapper {
	int deleteByPrimaryKey(Long id);

	int insert(Vithdrawal record);

	int insertSelective(Vithdrawal record);

	Vithdrawal selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Vithdrawal record);

	int updateByPrimaryKey(Vithdrawal record);

	List<Vithdrawal> selectVithdrawalInfo(Map<String, Object> map);
}