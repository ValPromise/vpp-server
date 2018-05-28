package com.vpp.core.standardized.order.service;

import java.util.List;
import java.util.Map;

public interface IContractService {
    Map<String,Object> getContract(Map<String,Object> map);
    
    List<String> getHotCity();
    
    Map<String,String> getCityInfos();
    
    String getCityNameByCityId(String cityId);
    
    List<Map<String,String>> getCitys();
}
