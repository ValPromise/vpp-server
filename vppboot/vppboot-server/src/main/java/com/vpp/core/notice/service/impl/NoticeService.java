package com.vpp.core.notice.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.core.notice.bean.Notice;
import com.vpp.core.notice.mapper.NoticeMapper;
import com.vpp.core.notice.service.INoticeService;

@Service
public class NoticeService implements INoticeService {
    // private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public int deleteByPrimaryKey(Long id) throws Exception {
        return noticeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Notice record) throws Exception {
        return noticeMapper.insert(record);
    }

    @Override
    public Notice selectByPrimaryKey(Long id) throws Exception {
        return noticeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Notice record) throws Exception {
        return noticeMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page<Notice> findLimitByCond(int pageNum, int pageSize, String title) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        return noticeMapper.findLimitByCond(title);
    }

    @Override
    public List<Notice> findListByState() throws Exception {
        return noticeMapper.findListByState();
    }

    @Override
    public Page<Notice> findLimit(int pageNum, int pageSize, Map<String, Object> params) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        return noticeMapper.findLimit(params);
    }
}
