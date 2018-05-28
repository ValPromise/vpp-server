package com.vpp.common.utils;

import java.math.BigDecimal;
import java.util.Date;


public class DealUtil {
    /**
     * TODO 触发值和实况值对比
     * 
     * @author cgp
     * @param realWeather 实况值
     * @param threshold 触发值
     * @param opType 判断类型
     * @return
     */
    public static boolean weatherCompare(Float realWeather, Float threshold, int opType) {
        boolean compare = false;
        // nmc的值和触发值比 '1:小与;2:小于或等于;3:等于;4:大于或等于;5:大于',
        switch (opType) {
        case 1:// 实况值小于触发值
            compare = realWeather.compareTo(threshold) < 0;
            break;
        case 2:// 实况值小于等于触发值
            compare = realWeather.compareTo(threshold) <= 0;
            break;
        case 3:// 实况值等于触发值
            compare = realWeather.compareTo(threshold) == 0;
            break;
        case 4:// 实况值大于等于触发值
            compare = realWeather.compareTo(threshold) >= 0;
            break;
        case 5:// 实况值大于触发值
            compare = realWeather.compareTo(threshold) > 0;
            break;
        }
        return compare;
    }

    public static String createId(String prefix) {
        String dealId = String.valueOf(System.nanoTime());
        dealId = dealId.substring(dealId.length() - 9, dealId.length() - 1);
        int endRandom = (int) (Math.random() * 9000 + 1000);
        dealId = prefix + dealId + endRandom;

        return dealId.replaceFirst("0", "1");
    }

    public static String checkTomorrow(String date) {
        // 判断保障开始时间和结束时间是否合法 开始时间只能从明天开始
        Date today = DateUtil.parseDate(new Date(), DateUtil.YMD_DATE_TIME_PATTERN);
        Date sDate = DateUtil.parseDate(date, DateUtil.YMD_DATE_TIME_PATTERN);
        if (!sDate.after(today)) {
            return "保障开始时间必须从明天开始。";
        }
        return null;
    }
    
    /**
     * BigDecimal 加法
     * @author cgp
     * @param price 原有金额
     * @param addPriace 需要增加的金额
     * @return
     */
    public static BigDecimal priceAdd(BigDecimal price,BigDecimal addPriace){
        if(price ==null){
            price = new BigDecimal(0);
        }
        if(addPriace ==null){
            addPriace = new BigDecimal(0);
        }
        return price.add(addPriace);
    }
    
    /**
     * BigDecimal 减法
     * @author cgp
     * @param price 原有金额
     * @param subtractPriace 需要减去的金额
     * @return
     */
    public static BigDecimal priceSubtract(BigDecimal price,BigDecimal subtractPriace){
        if(price ==null){
            price = new BigDecimal(0);
        }
        if(subtractPriace ==null){
            subtractPriace = new BigDecimal(0);
        }
        return price.subtract(subtractPriace);
    }
    
    /**
     * 
     * 比较金额大小  ${price} ${compareType} ${comparePrice} 例子: 1.1 > 1.0  返回ture
     * @author cgp
     * @param price 
     * @param comparePrice
     * @param opType
     * @return
     */
    public static boolean priceCompare(BigDecimal price, BigDecimal comparePrice, String compareType) {
        if(price == null || comparePrice == null){
            return false;
        }
        boolean compare = false;
        switch (compareType) {
        case "<":// price 小于 comparePrice
            compare = price.compareTo(comparePrice) < 0;
            break;
        case "<=":
            compare = price.compareTo(comparePrice) <= 0;
            break;
        case "=":
            compare = price.compareTo(comparePrice) == 0;
            break;
        case ">=":
            compare = price.compareTo(comparePrice) >= 0;
            break;
        case ">":
            compare = price.compareTo(comparePrice) > 0;
            break;
        }
        return compare;
    }
    
    /**
     * 
     * 根据weatherType 获取 对应文字描述
     * @author cgp
     * @param weatherType
     * @return
     */
    public static String getWeatherTypeStr(int weatherType){
        String weatherTypeStr = "";
        // 1-最高气温;2-最低气温;3-平均气温;4-降雨;5-风力
        switch (weatherType) {
        case 1:// 实况值小于触发值
            weatherTypeStr = "最高温度";
            break;
        case 2:// 实况值小于等于触发值
            weatherTypeStr = "最低温度";
            break;
        case 3:// 实况值等于触发值
            weatherTypeStr = "平均温度";
            break;
        case 4:// 实况值大于等于触发值
            weatherTypeStr = "降水量";
            break;
        case 5:// 实况值大于触发值
            weatherTypeStr = "风力";
            break;
        }
        return weatherTypeStr;
    }
    
    /**
     * 
     * 根据weatherType 获取 对应文字描述
     * @author cgp
     * @param weatherType
     * @return
     */
    public static String getWeatherTypeSuffix(int weatherType){
        String weatherTypeStr = "";
        // 1-最高气温;2-最低气温;3-平均气温;4-降雨;5-风力
        switch (weatherType) {
        case 1:
            weatherTypeStr = "℃";
            break;
        case 2:
            weatherTypeStr = "℃";
            break;
        case 3:
            weatherTypeStr = "℃";
            break;
        case 4:
            weatherTypeStr = "mm";
            break;
        case 5:// 实况值大于触发值
            weatherTypeStr = "";
            break;
        }
        return weatherTypeStr;
    }
    
    public static String getOpTypeStr(int opType){
        String opTypeStr = "";
     // nmc的值和触发值比 '1:小与;2:小于或等于;3:等于;4:大于或等于;5:大于',
        switch (opType) {
        case 1:// 实况值小于触发值
            opTypeStr = "<";
            break;
        case 2:// 实况值小于等于触发值
            opTypeStr = "≤";
            break;
        case 3:// 实况值等于触发值
            opTypeStr = "=";
            break;
        case 4:// 实况值大于等于触发值
            opTypeStr = "≥";
            break;
        case 5:// 实况值大于触发值
            opTypeStr = ">";
            break;
        }
        return opTypeStr;
    }
    
    /**
     * BigDecimal 乘法
     * @param price 原有金额
     * @param multiplyPriace 倍数
     * @return
     */
    public static BigDecimal priceMultiply(BigDecimal price,BigDecimal multiplyPriace){
    	return price.multiply(multiplyPriace);
    }
    
    /***
     * 除法
     * @param price 除数
     * @param dividePriace 被除数
     * @return
     */
    public static BigDecimal priceDivide(BigDecimal price,BigDecimal dividePriace){
    	return price.divide(dividePriace);
    }
    
    public static void main(String[] args) {
        BigDecimal price= new BigDecimal(9.223372036854775807);
    	BigDecimal priceprice = new BigDecimal(50000);
    	BigDecimal dd = DealUtil.priceDivide(price, priceprice);
    	BigDecimal ddr =  DealUtil.priceMultiply(dd,new BigDecimal(569.43));
    	double rerr =  DealUtil.priceMultiply(ddr, new BigDecimal(6.3317)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    	System.out.println(rerr);
    	 
	}
}
//0.000184467440737095529357247869484126567840576171875