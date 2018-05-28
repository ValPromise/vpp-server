package com.vpp.core.notice.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.vpp.core.notice.bean.Notice;

@Mapper
public interface NoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Notice record);

    Notice selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Notice record);

    Page<Notice> findLimitByCond(@Param("title") String title);

    Page<Notice> findLimit(Map<String, Object> params);

    List<Notice> findListByState();
}