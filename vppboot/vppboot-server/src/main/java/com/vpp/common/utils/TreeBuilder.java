/*
 * 文  件  名：TreeBuilder.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月9日
 * 修改内容：新增
 */
package com.vpp.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.vpp.core.system.resource.bean.MenuVo;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 封装树形集合
 * 
 * @author Lxl
 * @version V1.0 2018年5月9日
 */
public class TreeBuilder {

    @SuppressWarnings("unchecked")
    public List<MenuVo> buildListToTree(List<MenuVo> dirs) {
        List<MenuVo> roots = findRoots(dirs);
        List<MenuVo> notRoots = (List<MenuVo>) CollectionUtils.subtract(dirs, roots);
        for (MenuVo root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    public List<MenuVo> findRoots(List<MenuVo> allMenuVos) {
        List<MenuVo> results = new ArrayList<MenuVo>();
        for (MenuVo node : allMenuVos) {
            boolean isRoot = true;
            for (MenuVo comparedOne : allMenuVos) {
                if (node.getParentId() == comparedOne.getId()) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                // node.setLevel(0);
                results.add(node);
                // node.setRootId(node.getId());
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private List<MenuVo> findChildren(MenuVo root, List<MenuVo> allMenuVos) {
        List<MenuVo> children = new ArrayList<MenuVo>();

        for (MenuVo comparedOne : allMenuVos) {
            if (comparedOne.getParentId() == root.getId()) {
                // comparedOne.setParent(root);
                // comparedOne.setLevel(root.getLevel() + 1);
                children.add(comparedOne);
            }
        }
        List<MenuVo> notChildren = (List<MenuVo>) CollectionUtils.subtract(allMenuVos, children);
        for (MenuVo child : children) {
            List<MenuVo> tmpChildren = findChildren(child, notChildren);
            if (tmpChildren == null || tmpChildren.size() < 1) {
                // child.setLeaf(true);
            } else {
                // child.setLeaf(false);
            }
            child.setChildren(tmpChildren);
        }
        return children;
    }

    public static void main(String[] args) {
        TreeBuilder tb = new TreeBuilder();
        List<MenuVo> allMenuVos = new ArrayList<MenuVo>();
        allMenuVos.add(new MenuVo(1L, 0L, "节点1"));
        allMenuVos.add(new MenuVo(2L, 0L, "节点2"));
        allMenuVos.add(new MenuVo(3L, 0L, "节点3"));
        allMenuVos.add(new MenuVo(4L, 1L, "节点4"));
        allMenuVos.add(new MenuVo(5L, 1L, "节点5"));
        allMenuVos.add(new MenuVo(6L, 1L, "节点6"));
        allMenuVos.add(new MenuVo(7L, 4L, "节点7"));
        allMenuVos.add(new MenuVo(8L, 4L, "节点8"));
        allMenuVos.add(new MenuVo(9L, 5L, "节点9"));
        allMenuVos.add(new MenuVo(10L, 100L, "节点10"));
        List<MenuVo> roots = tb.buildListToTree(allMenuVos);

        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONArray jsonarray = JSONArray.fromObject(roots, jsonConfig);
        System.out.println(jsonarray);

    }
}