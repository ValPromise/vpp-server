package com.vpp.core.standardized.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.HttpUtils;
import com.vpp.core.standardized.order.service.IContractService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ContractService implements IContractService{
    private static final Logger logger = LogManager.getLogger(ContractService.class);
    
    private static final String PYTHON_URL = "http://119.28.70.201:8977/contract/";
    
    private static final String CONTRACT_TITLE = "{0} 全天{1}最高气温是";
    @Autowired
    private RedisTemplate<String, String> searchRedis;
    @Override
    public Map<String, Object> getContract(Map<String, Object> map) {
        Gson gson = new Gson();
        Map<String,Object> returnMap = new HashMap<String, Object>();
        String jsonStr = "";
        String calculationParams = JSONObject.fromObject(map).toString();
        try {
            Date stime = new Date();
            try {
                jsonStr = HttpUtils.post(PYTHON_URL, map);
                logger.info("合约查询用时===="+ DateUtil.diffDateTime(stime, new Date()));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            String code = jsonObject.getString("code");
            if (null != code && "0".equals(code)) {// 合约查询成功状态码
                JSONArray resultJsonObj = JSONArray.fromObject(jsonObject.get("result"));
                String contractId = DealUtil.createId("V");
                Iterator<Object> it = resultJsonObj.iterator();
                returnMap.put("contractId", contractId);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                String searchDate = DateUtil.format(new Date(), DateUtil.LONG_DATE_TIME_PATTERN_SSS);
                while (it.hasNext()) {
                    JSONObject ob = (JSONObject) it.next();
                    Map<String,Object> cacheMap = new HashMap<String, Object>();
                    cacheMap.put("strike",ob.get("Strike").toString());
                    cacheMap.put("payoff", ob.get("payoff").toString());
                    cacheMap.put("price", ob.get("price").toString());
                    cacheMap.put("cityId", map.get("city_id"));
                    cacheMap.put("time", DateUtil.strToStr(map.get("date_ref").toString(), DateUtil.YMD_DATE_TIME_PATTERN, DateUtil.YMD_DATE_PATTERN));
                    cacheMap.put("validity", ob.get("validity")!=null?ob.get("validity").toString():0);
                    cacheMap.put("searchDate", searchDate);
                    cacheMap.put("template_id", map.get("template_id"));
                    searchRedis.opsForHash().put(contractId, ob.get("Strike").toString(), gson.toJson(cacheMap).toString());
                    Map<String,Object> contract = new HashMap<String, Object>();
                    String validity = ob.get("validity")!=null?ob.get("validity").toString():"0";
                    if("0".equals(validity)){
                        continue;
                    }
                    contract.put("price", ob.get("price"));
                    contract.put("payoff", ob.get("payoff"));
                    contract.put("validity", validity);
                    contract.put("strike", ob.get("Strike"));
                    contract.put("title", this.getCityNameByCityId(map.get("city_id").toString())+DateUtil.strToStr(map.get("date_ref").toString(), DateUtil.YMD_DATE_TIME_PATTERN, "MMdd"));
                    int opType = ob.get("opType") == null?5:(int)ob.get("opType");
                    contract.put("condition", DealUtil.getOpTypeStr(opType)+ob.get("Strike")+"℃");
                    list.add(contract);
                }
                returnMap.put("contractList", list);
                return returnMap;
            }
        } catch (Exception e) {
            logger.error("selectContract error::{}---{}--{}", e.getMessage(), jsonStr, calculationParams);
            return null;
        }
        return null;
    }
    
    @Override
    public List<String> getHotCity() {
        List<String> list = new ArrayList<String>();
        list.add("CN54511");
        list.add("CN58367");
        list.add("CN59493");
        list.add("CN59287");
        return list;
    }
    
    
    @Override
    public Map<String,String> getCityInfos(){
        Map<String,String> cityInfos = new HashMap<String, String>();
        cityInfos.put("CN54511", "北京");
        cityInfos.put("CN58367", "上海");
        cityInfos.put("CN59493", "深圳");
        cityInfos.put("CN59287", "广州");
        return cityInfos;
    }

    @Override
    public String getCityNameByCityId(String cityId) {
        Map<String, String> map = this.getCityInfos();
        return cityId!=null?map.get(cityId):"";
    }

    @Override
    public List<Map<String, String>> getCitys() {
        Map<String,String> cityInfos = this.getCityInfos();
        List<String> list = this.getHotCity();
        List<Map<String,String>> retList = new ArrayList<Map<String,String>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> cityMap = new HashMap<String, String>();
            cityMap.put("cityId", list.get(i));
            cityMap.put("cityName", cityInfos.get(list.get(i)));
            retList.add(cityMap);
        }
        return retList;
    }
}
