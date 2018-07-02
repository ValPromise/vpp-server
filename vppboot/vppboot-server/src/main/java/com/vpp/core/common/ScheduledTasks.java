/*
 * 文  件  名：ScheduledTasks.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月24日
 * 修改内容：新增
 */
package com.vpp.core.common;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vpp.common.utils.DateUtil;
import com.vpp.core.coinguess.service.ICoinguessService;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.rain.service.IRainOrderTriggerService;
import com.vpp.core.standardized.trigger.ITriggerService;
import com.vpp.core.withdrawal.service.IWithdrawalService;

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
    @Autowired
    private IWithdrawalService withdrawalService;

    @Autowired
    private ICoinguessService coinguessService;

    @Autowired
    private ITriggerService triggerService;
    @Autowired
    private IRainOrderTriggerService rainOrderTriggerService;

    @Scheduled(cron = "35 * * * * ?") // 每到35整秒执行一次，1分35秒；2分35秒；3分35秒；4分35秒...
    // 批量获取下单价格，第35秒
    public void batchUpdateOrderPrice35() {
        System.out.println("batchUpdateOrderPrice35: " + new Date());
        try {
            Long currentLotteyTime = ((System.currentTimeMillis() / 60000L) + 1L) * 60000L; // 推算当前开奖时间，60000 ms = 1分钟
            coinguessService.batchUpdateOrderPrice(currentLotteyTime);
        } catch (Exception e) {
            logger.error("猜币批量更新下单价格出错35 : " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Scheduled(cron = "46 * * * * ?") // 每到46整秒执行一次，1分46秒；2分46秒；3分46秒；4分46秒...
    // 批量获取下单价格，第46秒
    public void batchUpdateOrderPrice46() {
        System.out.println("batchUpdateOrderPrice46: " + new Date());
        try {
            Long currentLotteyTime = ((System.currentTimeMillis() / 60000L) + 1L) * 60000L; // 推算当前开奖时间，60000 ms = 1分钟
            coinguessService.batchUpdateOrderPrice(currentLotteyTime);
        } catch (Exception e) {
            logger.error("猜币批量更新下单价格出错46 : " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Scheduled(cron = "20 * * * * ?") // 每到20整秒执行一次，1分20秒；2分20秒；3分20秒；4分20秒...
    // 定时开奖
    public void batchLottery() {
        System.out.println("batchLottery: " + new Date());
        try {
            coinguessService.batchLottery(System.currentTimeMillis());
        } catch (Exception e) {
            logger.error("猜币定时开奖出错 : " + e.getMessage());
            // e.printStackTrace();
        }
    }

    // 触发
    @Scheduled(cron = "0 17 8 * * ?") // 每到20整秒执行一次，1分20秒；2分20秒；3分20秒；4分20秒...
    public void trigger() {
        Date etimeDate = DateUtil.diffDate(new Date(), 2);
        String etime = DateUtil.format(etimeDate, DateUtil.YMD_DATE_PATTERN);
        logger.info("task trigger ::: {}", etime);
        try {
            triggerService.triggerByEtime(etime);
        } catch (Exception e) {
            logger.error("温度合约触发错误: " + e.getMessage());
        }

        try {
            rainOrderTriggerService.triggerByEtime(etime);
        } catch (Exception e) {
            logger.error("降雨合约触发错误: " + e.getMessage());
        }
    }

    @Scheduled(cron = "30 * * * * ?") // 每到30整秒执行一次，1分30秒；2分30秒；3分30秒；4分30秒...
    // 批量退款
    public void batchRefund() {
        System.out.println("batchRefund: " + new Date());
        try {
            coinguessService.batchRefund();
        } catch (Exception e) {
            logger.error("批量退款出错 : " + e.getMessage());
            // e.printStackTrace();
        }
    }

    private int fixedDelayCount = 1;
    // private int fixedRateCount = 1;
    // private int initialDelayCount = 1;
    // private int cronCount = 1;

    @Scheduled(fixedDelay = 1000 * 10) // fixedDelay = 5000表示当前方法执行完毕5000ms后，Spring scheduling会再次调用该方法
    public void cacheEthUsdt() {
        // logger.info("cacheEthUsdt...");
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
        // logger.info("cacheUsdToCny...");
        // 缓存eth美元价格
        try {
            customerService.cacheUsdToCny();
        } catch (Exception e) {
            logger.error("获取美元汇率错误  cacheUsdToCny : {}" + e.getMessage());
        }
        // logger.info("===fixedDelay: 第{}次执行方法", fixedDelayCount++);
    }

    /**
     * 同步提现链上提款数据
     * 
     * @author Lxl
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5) // 每分钟执行
    public void syncWithdrawalData() {
        try {
            withdrawalService.syncWithdrawalData();
        } catch (Exception e) {
            logger.error("同步提现链上处理数据  syncWithdrawalData : {}" + e.getMessage());
        }
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

    // public static void main(String[] args){
    // Date etimeDate = DateUtil.diffDate(new Date(), 2);
    // System.out.println(etimeDate);
    // }

}
