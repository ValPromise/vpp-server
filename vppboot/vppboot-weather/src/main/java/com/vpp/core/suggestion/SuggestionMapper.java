package com.vpp.core.suggestion;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface SuggestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Suggestion record);

    int insertSelective(Suggestion record);

    Suggestion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Suggestion record);

    int updateByPrimaryKey(Suggestion record);
}