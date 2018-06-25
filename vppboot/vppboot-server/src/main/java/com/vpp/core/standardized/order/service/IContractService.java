package com.vpp.core.standardized.order.service;

import java.util.List;
import java.util.Map;

import com.vpp.core.standardized.order.bean.DataModelDto;

public interface IContractService {
    /**
     * 查询高温合约
     * 
     * @author Lxl
     * @param map
     * @return
     */
    Map<String, Object> getContract(Map<String, Object> map);

    List<String> getHotCity();

    Map<String, String> getCityInfos();

    String getCityNameByCityId(String cityId);

    List<Map<String, String>> getCitys();

    /**
     * 查询 降雨合约
     * 
     * @author Lxl
     * @param cityId cityID CN54511
     * @param startTime 开始日期 20180528
     * @param endTime 结束日期 20180529
     * @param userCode 用户标识 手机号码/token ...
     * @return
     * @throws Exception
     */
    DataModelDto selectTemplateId108(String cityId, String startTime, String endTime, String userCode) throws Exception;
}
