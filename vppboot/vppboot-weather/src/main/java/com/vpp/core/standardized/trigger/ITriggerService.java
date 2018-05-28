package com.vpp.core.standardized.trigger;


public interface ITriggerService {
    
    /**
     * 根据日期判断触发 ，适用于数据更正，会保留原触发数据 （慎用）
     * @author cgp
     * @param etime
     * @throws Exception
     */
    void triggerByEtime(String etime) throws Exception;
    
    OrderTrigger getTriggerByInnerOrderId(String innerOrderId);
}
