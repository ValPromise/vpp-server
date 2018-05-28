package com.vpp.core.system.role.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.LoginUtils;
import com.vpp.common.utils.ObjectUtils;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.system.resource.bean.Resource;
import com.vpp.core.system.resource.service.IResourceService;
import com.vpp.core.system.role.bean.Role;
import com.vpp.core.system.role.bean.RoleResource;
import com.vpp.core.system.role.service.IRoleResourceService;
import com.vpp.core.system.role.service.IRoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
    private static final Logger logger = LogManager.getLogger(RoleController.class);

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResourceService resourceService;;
    @Autowired
    private IRoleResourceService roleResourceService;;
    @Autowired
    LoginUtils loginUtils;

    @RequestMapping(value = "/limit")
    @ResponseBody
    public Map<String, Object> limit(Integer page, Integer rows, String title, HttpServletResponse response) {
        page = page == null ? 1 : page;
        rows = rows == null ? 20 : rows;
        long total = 0;

        Page<Role> list = null;
        try {
            list = roleService.findLimitByCond(page, rows);
        } catch (Exception e) {
            logger.error("listNotice error ::: {}", e.getMessage());
        }
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        if (total == 0) {// 取一次总页数
            total = pageInfo.getTotal();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("page", page);
        result.put("rows", pageInfo.getList());
        return result;
    }

    @RequestMapping(value = "/findResourceIdsByRoleId")
    @ResponseBody
    public ResultVo findResourceIdsByRoleId(Long roleId, HttpServletResponse response) {
        List<Long> roleIds = new ArrayList<Long>();
        roleIds.add(roleId);
        try {
            List<Resource> resources = resourceService.findParentListByRoleIds(roleIds);
            List<Long> returnRoleIds = new ArrayList<Long>();
            for (Resource rs : resources) {
                returnRoleIds.add(rs.getId());
            }
            return ResultVo.setResultSuccess(returnRoleIds);
        } catch (Exception e) {
            logger.error("findResourceIdsByRoleId error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
    }

    @RequestMapping(value = "/updateResource")
    @ResponseBody
    public ResultVo updateResource(Long roleId,String resourceIds, HttpServletResponse response,
            HttpServletRequest request) {
        try {
//            resourceIds  =request.getParameterValues("resourceIds[]");
            if (ObjectUtils.isNull(roleId, resourceIds)) {
                return ResultVo.setResultError("params error.");
            }
            List<RoleResource> list = new ArrayList<RoleResource>();
            for (String resourceId : resourceIds.split(",")) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(Long.valueOf(resourceId));
                list.add(roleResource);
            }
            roleResourceService.update(roleId, list);
            return ResultVo.setResultSuccess();
        } catch (Exception e) {
            logger.error("findResourceIdsByRoleId error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
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
    public ResultVo insert(String name, String description, Byte seq, HttpServletRequest request) {
        Role bean = new Role();
        bean.setName(name);
        bean.setDescription(description);
        bean.setStatus(ConstantsServer.ROLE_STATE_ENABLE);
        bean.setSeq(seq);
        try {
            roleService.insert(bean);
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
    public ResultVo update(Long id, String name, String description, Byte seq, Byte status, HttpServletRequest request) {
        try {
            // 获取当前登录用户
            Role bean = roleService.selectByPrimaryKey(id);
            bean.setName(name);
            bean.setDescription(description);
            bean.setStatus(status);
            bean.setSeq(seq);
            int count = roleService.updateByPrimaryKey(bean);
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
            roleService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("delete error ::: {}", e.getMessage());
            return ResultVo.setResultError(e.getMessage());
        }
        return ResultVo.setResultSuccess();
    }
}
