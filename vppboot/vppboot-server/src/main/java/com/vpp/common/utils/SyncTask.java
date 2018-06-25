/*
 * 文  件  名：SyncTask.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年6月20日
 * 修改内容：新增
 */
package com.vpp.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.vpp.core.deposit.service.IDepositService;

/**
 * 异步执行任务
 * 
 * @author Lxl
 * @version V1.0 2018年6月20日
 */
@Component
public class SyncTask {

    private static final Logger logger = LogManager.getLogger(SyncTask.class);

    @Autowired
    private IDepositService depositService;

    // 任务3;
    @Async
    public void syncDepositByAccount(String account) {
        try {
            // 查询充值记录入库
            depositService.syncDepositByAccount(account);
        } catch (Exception e) {
            // logger.error(e.getMessage());
        }
    }

}