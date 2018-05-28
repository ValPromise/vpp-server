package com.vpp.core.notice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.LoginUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.notice.bean.Notice;
import com.vpp.core.notice.service.INoticeService;

/**
 * 系统公告管理
 * 
 * @author Lxl
 * @version V1.0 2018年4月18日
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    private static final Logger logger = LogManager.getLogger(NoticeController.class);

    @Autowired
    private INoticeService noticeService;
    @Autowired
    LoginUtils loginUtils;

    /**
     * 分页查询
     * 
     * @author Lxl
     * @param page
     * @param rows
     * @param title
     * @param response
     * @return
     */
    @RequestMapping(value = "/listNotice")
    @ResponseBody
    public Map<String, Object> listNotice(Integer page, Integer rows, String title, HttpServletResponse response) {
        page = page == null ? 1 : page;
        rows = rows == null ? 20 : rows;
        long total = 0;

        Page<Notice> list = null;
        try {
            list = noticeService.findLimitByCond(page, rows, title);
        } catch (Exception e) {
            logger.error("listNotice error ::: {}", e.getMessage());
        }
        PageInfo<Notice> pageInfo = new PageInfo<>(list);
        if (total == 0) {// 取一次总页数
            total = pageInfo.getTotal();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("page", page);
        result.put("rows", pageInfo.getList());
        return result;
    }

    /**
     * 新增
     * 
     * @author Lxl
     * @param noticeTitle
     * @param noticeContent
     * @return
     */
    @RequestMapping(value = "/insert")
    @ResponseBody
    public ResultVo insert(String noticeTitle, String noticeContent, Byte noticeState, HttpServletRequest request) {
        // 获取当前登录用户
        Long account = loginUtils.getLoginAccount(request);
        Notice notice = new Notice();
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);
        notice.setNoticeState(noticeState);
        notice.setGmtCreate(DateUtil.getCurrentDateTimeLocal());
        notice.setCreater(account);
        try {
            noticeService.insert(notice);
        } catch (Exception e) {
            logger.error("insert error ::: {}", e.getMessage());
            ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }

    /**
     * 修改
     * 
     * @author Lxl
     * @param id
     * @param noticeTitle
     * @param noticeContent
     * @param noticeState
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultVo update(Long id, String noticeTitle, String noticeContent, Byte noticeState, HttpServletRequest request) {
        try {
            // 获取当前登录用户
            Long account = loginUtils.getLoginAccount(request);
            Notice notice = noticeService.selectByPrimaryKey(id);
            notice.setNoticeTitle(noticeTitle);
            notice.setNoticeContent(noticeContent);
            notice.setNoticeState(noticeState);
            notice.setMender(account);
            notice.setGmtModified(DateUtil.getCurrentDateTimeLocal());
            noticeService.updateByPrimaryKey(notice);
        } catch (Exception e) {
            logger.error("update error ::: {}", e.getMessage());
            ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }

    /**
     * 删除
     * 
     * @author Lxl
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultVo delete(Long id) {
        try {
            noticeService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("delete error ::: {}", e.getMessage());
            ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }
}
