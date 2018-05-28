package com.vpp.core.notice.app;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.vo.ResultVo;
import com.vpp.core.notice.bean.Notice;
import com.vpp.core.notice.service.INoticeService;

/**
 * APP系统公告
 * 
 * @author Lxl
 * @version V1.0 2018年4月18日
 */
@RestController
@RequestMapping("/app/notice")
public class AppNoticeController {
    private static final Logger logger = LogManager.getLogger(AppNoticeController.class);

    @Autowired
    private INoticeService noticeService;

    /**
     * app查询系统公告接口
     * 
     * @author Lxl
     * @param page
     * @param rows
     * @param title
     * @param response
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public ResultVo list(Integer page, Integer rows, String title, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        try {
            List<Notice> list = noticeService.findListByState();
            return ResultVo.setResultSuccess(list);
        } catch (Exception e) {
            logger.error("list error:::{}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
    }

}
