/*
 * 文  件  名：MenuVo.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月9日
 * 修改内容：新增
 */
package com.vpp.core.system.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单展示VO，分级
 * 
 * @author Lxl
 * @version V1.0 2018年5月9日
 */
public class MenuVo implements Serializable {
    /**
     * TODO 添加字段注释
     */
    private static final long serialVersionUID = 8628405172482354621L;
    /*
     * id: 21, label: "系统公告管理", href: '/admin/notice', icon: 'icon-caidanguanli', meta: {}, children: []
     */
    private Long id;
    private Long parentId;
    private String label;
    private String href;
    private String icon;
    private Object meta = new Object();
    private List<MenuVo> children = new ArrayList<MenuVo>();

    public MenuVo() {
    }

    public MenuVo(Long id, Long parentId, String label) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
