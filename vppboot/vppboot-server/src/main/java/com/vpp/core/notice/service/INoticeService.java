package com.vpp.core.notice.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.vpp.core.notice.bean.Notice;

/**
 * 系统公告
 * 
 * @author Lxl
 * @version V1.0 2018年4月18日
 */
public interface INoticeService {
    int deleteByPrimaryKey(Long id) throws Exception;

    int insert(Notice record) throws Exception;

    Notice selectByPrimaryKey(Long id) throws Exception;

    int updateByPrimaryKey(Notice record) throws Exception;

    Page<Notice> findLimitByCond(int pageNum, int pageSize, String title) throws Exception;

    Page<Notice> findLimit(int pageNum, int pageSize, Map<String, Object> params) throws Exception;

    List<Notice> findListByState() throws Exception;
}
