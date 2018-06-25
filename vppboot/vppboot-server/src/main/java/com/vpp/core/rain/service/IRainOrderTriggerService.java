package com.vpp.core.rain.service;

/**
 * 降雨合约触发
 * 
 * @author Lxl
 * @version V1.0 2018年5月29日
 */
public interface IRainOrderTriggerService {

    /**
     * 根据结束时间重触发判断
     * 
     * @author Lxl
     * @param etime
     * @throws Exception
     */
    void triggerByEtime(String etime) throws Exception;
}
