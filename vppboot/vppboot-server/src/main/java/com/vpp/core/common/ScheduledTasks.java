/*
 * 文  件  名：ScheduledTasks.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月24日
 * 修改内容：新增
 */
package com.vpp.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vpp.core.customer.service.ICustomerService;

/**
 * 定时任务
 * 
 * @author Lxl
 * @version V1.0 2018年5月24日
 */
@Component
public class ScheduledTasks {
    private static final Logger logger = LogManager.getLogger(ScheduledTasks.class);

    @Autowired
    private ICustomerService customerService;

    private int fixedDelayCount = 1;
    // private int fixedRateCount = 1;
    // private int initialDelayCount = 1;
    // private int cronCount = 1;

    @Scheduled(fixedDelay = 1000 * 10) // fixedDelay = 5000表示当前方法执行完毕5000ms后，Spring scheduling会再次调用该方法
    public void cacheEthUsdt() {
//        logger.info("cacheEthUsdt...");
        // 缓存eth美元价格
        try {
            customerService.cacheEthUsdt();
        } catch (Exception e) {
            logger.error("获取ETH实时行情错误  cacheEthUsdt : {}" + e.getMessage());
        }
        // logger.info("===fixedDelay: 第{}次执行方法", fixedDelayCount++);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60) // fixedDelay = 5000表示当前方法执行完毕5000ms后，Spring scheduling会再次调用该方法
    public void cacheUsdToCny() {
//        logger.info("cacheUsdToCny...");
        // 缓存eth美元价格
        try {
            customerService.cacheUsdToCny();
        } catch (Exception e) {
            logger.error("获取美元汇率错误  cacheUsdToCny : {}" + e.getMessage());
        }
        // logger.info("===fixedDelay: 第{}次执行方法", fixedDelayCount++);
    }

    // @Scheduled(fixedRate = 5000) //fixedRate = 5000表示当前方法开始执行5000ms后，Spring scheduling会再次调用该方法
    // public void testFixedRate() {
    // logger.info("===fixedRate: 第{}次执行方法", fixedRateCount++);
    // }
    //
    // @Scheduled(initialDelay = 1000, fixedRate = 5000) //initialDelay = 1000表示延迟1000ms执行第一次任务
    // public void testInitialDelay() {
    // logger.info("===initialDelay: 第{}次执行方法", initialDelayCount++);
    // }
    //
    // @Scheduled(cron = "0 0/1 * * * ?") //cron接受cron表达式，根据cron表达式确定定时规则
    // public void testCron() {
    // logger.info("===initialDelay: 第{}次执行方法", cronCount++);
    // }
}
