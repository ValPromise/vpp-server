package com.vpp.core.standardized.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.vpp.common.utils.Constants;
import com.vpp.common.utils.ConstantsRain;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.utils.HttpUtils;
import com.vpp.common.utils.OrderUtil;
import com.vpp.core.standardized.order.bean.DataModelDto;
import com.vpp.core.standardized.order.service.IContractService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ContractService implements IContractService {
    private static final Logger logger = LogManager.getLogger(ContractService.class);

    private static final String PYTHON_URL = "http://119.28.70.201:8977/contract/";
    // private static final String RAIN_URL = "http://120.77.47.164:8891/contract/";// 合约查询地址
    private static final String RAIN_URL = "http://vpp.raincontract.baotianqi.cn/ziying/";// 合约查询地址
    // private static final String RAIN_URL_TEST = "http://120.77.47.164:8894/contract/";// 合约查询地址

    // private static final String CONTRACT_TITLE = "{0} 全天{1}最高气温是";
    @Autowired
    private RedisTemplate<String, String> searchRedis;

    @Value("${spring.profiles.active}")
    private String profiles;

    @Override
    public Map<String, Object> getContract(Map<String, Object> map) {
        Gson gson = new Gson();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String jsonStr = "";
        String calculationParams = JSONObject.fromObject(map).toString();
        try {
            Date stime = new Date();
            try {
                jsonStr = HttpUtils.post(PYTHON_URL, map);
                logger.info("合约查询用时====" + DateUtil.diffDateTime(stime, new Date()));
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
                    Map<String, Object> cacheMap = new HashMap<String, Object>();
                    cacheMap.put("strike", ob.get("Strike").toString());
                    cacheMap.put("payoff", ob.get("payoff").toString());
                    cacheMap.put("price", ob.get("price").toString());
                    cacheMap.put("cityId", map.get("city_id"));
                    cacheMap.put("time", DateUtil.strToStr(map.get("date_ref").toString(), DateUtil.YMD_DATE_TIME_PATTERN,
                            DateUtil.YMD_DATE_PATTERN));
                    cacheMap.put("validity", ob.get("validity") != null ? ob.get("validity").toString() : 0);
                    cacheMap.put("searchDate", searchDate);
                    cacheMap.put("template_id", map.get("template_id"));
                    searchRedis.opsForHash().put(contractId, ob.get("Strike").toString(), gson.toJson(cacheMap).toString());
                    searchRedis.expire(contractId, Constants.CACHE_TIME_MINUTE * 5, TimeUnit.SECONDS);// 缓存五分钟
                    Map<String, Object> contract = new HashMap<String, Object>();
                    String validity = ob.get("validity") != null ? ob.get("validity").toString() : "0";
                    if ("0".equals(validity)) {
                        continue;
                    }
                    contract.put("price", ob.get("price"));
                    contract.put("payoff", ob.get("payoff"));
                    contract.put("validity", validity);
                    contract.put("strike", ob.get("Strike"));
                    contract.put("title", this.getCityNameByCityId(map.get("city_id").toString())
                            + DateUtil.strToStr(map.get("date_ref").toString(), DateUtil.YMD_DATE_TIME_PATTERN, "MMdd"));
                    int opType = ob.get("opType") == null ? 5 : (int) ob.get("opType");
                    contract.put("condition", DealUtil.getOpTypeStr(opType) + ob.get("Strike") + "℃");
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
    public DataModelDto selectTemplateId108(String cityId, String startTime, String endTime, String userCode) throws Exception {
        Float threshold = null;
        Integer price = null;
        Integer payoutFee = null;
        // String url = "production".equals(profiles) ? RAIN_URL : RAIN_URL_TEST;
        String url = RAIN_URL;
        Map<String, Object> requestMap = new HashMap<String, Object>();
        // 根据条件查询触发值和合约价格 start ******************
        requestMap.put("template_id", ConstantsRain.TEMPLATE_ID);// 模板ID
        requestMap.put("city_id", cityId);// 城市ID
        requestMap.put("begin_date", startTime + "000000");// 开始时间 yyyyMMdd
        requestMap.put("end_date", endTime + "000000");// 结束时间 yyyyMMdd
        requestMap.put("user_code", userCode);
        // 请求合约计算相关
        String calculationParams = JSONObject.fromObject(requestMap).toString();
        String jsonStr = "";
        try {
            Date st = new Date();
            jsonStr = HttpUtils.post(url, requestMap);
            logger.info(DateUtil.format(new Date(), DateUtil.LONG_DATE_TIME_PATTERN) + " 合约查询用时"
                    + DateUtil.diffDateTime(new Date(), st) + "秒" + "请求地址====" + url);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            String code = jsonObject.getString("code");
            String innerOrderId = OrderUtil.createId(ConstantsRain.ORDERID_PREFIX);
            if (null != code && "0".equals(code)) {// 合约查询成功状态码
                DataModelDto dataModelDto = new DataModelDto();
                JSONObject resultJsonObj = JSONObject.fromObject(jsonObject.get("result"));
                threshold = Float.valueOf(resultJsonObj.get("threshold").toString());
                price = Integer.valueOf(Math.round(resultJsonObj.getDouble("price") * 100) + "");
                payoutFee = Integer.valueOf(Math.round(resultJsonObj.getDouble("payout") * 100) + "");
                if (null == threshold || null == price) {
                    logger.error("threshold or price is null ::: {} , {}", requestMap, jsonStr);
                    return null;
                }

                dataModelDto.setThreshold(threshold);
                dataModelDto.setPrice(price / 100);
                dataModelDto.setPayoutFee(payoutFee / 100);
                dataModelDto.setInnerOrderId(innerOrderId);
                dataModelDto.setCityId(cityId);
                dataModelDto.setStime(startTime);
                dataModelDto.setEtime(endTime);
                // this.updateSearchRedis(cityId, startTime, endTime, userCode);
                this.setContract108Redis(dataModelDto);// 缓存
                return dataModelDto;
            } else {
                logger.error("selectContract108 error data is null ::: {} , {}", requestMap, jsonStr);
            }
        } catch (Exception e) {
            logger.error("selectTemplateId108 error::{}---{}--{}", e.getMessage(), jsonStr, calculationParams);
        }
        return null;
    }

    /**
     * 缓存查询的降雨合约
     * 
     * @author Lxl
     * @param dto
     */
    private void setContract108Redis(DataModelDto dto) {
        // searchRedis.opsForValue().set(key, value, timeout, unit);
        Gson gson = new Gson();
        gson.toJson(dto);
        searchRedis.opsForValue().set(ConstantsRain.CONTRACT_CACHE_PREFIX + dto.getInnerOrderId(), gson.toJson(dto),
                Constants.CACHE_TIME_MINUTE * 5, TimeUnit.SECONDS);
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
    public Map<String, String> getCityInfos() {
        Map<String, String> cityInfos = new HashMap<String, String>();
        cityInfos.put("CN54511", "北京");
        cityInfos.put("CN58367", "上海");
        cityInfos.put("CN59493", "深圳");
        cityInfos.put("CN59287", "广州");
        return cityInfos;
    }

    @Override
    public String getCityNameByCityId(String cityId) {
        Map<String, String> map = this.getCityInfos();
        return cityId != null ? map.get(cityId) : "";
    }

    @Override
    public List<Map<String, String>> getCitys() {
        Map<String, String> cityInfos = this.getCityInfos();
        List<String> list = this.getHotCity();
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> cityMap = new HashMap<String, String>();
            cityMap.put("cityId", list.get(i));
            cityMap.put("cityName", cityInfos.get(list.get(i)));
            retList.add(cityMap);
        }
        return retList;
    }
}
