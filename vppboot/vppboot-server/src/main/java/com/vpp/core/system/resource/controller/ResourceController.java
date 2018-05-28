package com.vpp.core.system.resource.controller;

import java.util.HashMap;
import java.util.List;
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
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.LoginUtils;
import com.vpp.common.utils.ObjectUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.system.resource.bean.MenuVo;
import com.vpp.core.system.resource.bean.Resource;
import com.vpp.core.system.resource.service.IResourceService;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    private static final Logger logger = LogManager.getLogger(ResourceController.class);

    @Autowired
    private IResourceService resourceService;
    @Autowired
    LoginUtils loginUtils;

    private static final int DEFAULT_PID = 0;

    @RequestMapping(value = "/limit")
    @ResponseBody
    public Map<String, Object> limit(Integer page, Integer rows, String title, Long pid, HttpServletResponse response) {
        page = page == null ? 1 : page;
        rows = rows == null ? 20 : rows;
        long total = 0;

        Page<Resource> list = null;
        try {
            list = resourceService.findLimitByCond(page, rows, pid);

            // menus = loginUtils.getMenuTreeData(resourceService.findAll());
        } catch (Exception e) {
            logger.error("listNotice error ::: {}", e.getMessage());
        }
        PageInfo<Resource> pageInfo = new PageInfo<>(list);
        if (total == 0) {// 取一次总页数
            total = pageInfo.getTotal();
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("page", page);
        result.put("rows", pageInfo.getList());
        // result.put("menus", menus);
        return result;
    }

    @RequestMapping(value = "/loadTree")
    @ResponseBody
    public Map<String, Object> loadTree(HttpServletResponse response) {
        List<MenuVo> menus = null;
        try {
            menus = loginUtils.getMenuTreeData(resourceService.findAll());
        } catch (Exception e) {
            logger.error("loadTree error ::: {}", e.getMessage());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("menus", menus);
        result.put("pid", DEFAULT_PID);
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
    public ResultVo insert(String name, String url, String description, Long pid, Byte seq, Byte status,
            HttpServletRequest request) {
        if (!ObjectUtils.isNotNull(name, pid)) {
            return ResultVo.setResultError("params error.");
        }
        Resource bean = new Resource();
        bean.setName(name);
        bean.setUrl(url);
        bean.setDescription(description);
        bean.setStatus(status);
        bean.setSeq(seq);
        bean.setPid(pid);
        bean.setCreateTime(DateUtil.getCurrentDateTimeLocal());
        try {
            resourceService.insert(bean);
        } catch (Exception e) {
            logger.error("insert error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
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
    public ResultVo update(Long id, String name, String url, String description, Byte seq, Byte status,
            HttpServletRequest request) {
        try {
            // 获取当前登录用户
            Resource bean = resourceService.selectByPrimaryKey(id);
            bean.setName(name);
            bean.setUrl(url);
            bean.setDescription(description);
            bean.setStatus(status);
            bean.setSeq(seq);
            int count = resourceService.updateByPrimaryKey(bean);
            if (count <= Constants.ZERO) {
                return ResultVo.setResultError();
            }
        } catch (Exception e) {
            logger.error("update error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
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
            resourceService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("delete error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }

}
