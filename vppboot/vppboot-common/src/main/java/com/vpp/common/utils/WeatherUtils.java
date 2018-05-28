package com.vpp.common.utils;


public class WeatherUtils {
    // 天气：1-最高气温;2-最低气温;3-平均气温;4-降雨;5-风力
    /**
     * 获取符号 中文
     * 
     * @author Lxl
     * @param weather
     * @return
     */
    public static String getSymbolChinese(Byte weather) {
        if (1 == weather.intValue()) {
            return "最高气温";
        } else if (2 == weather.intValue()) {
            return "最低气温";
        } else if (3 == weather.intValue()) {
            return "平均气温";
        } else if (4 == weather.intValue()) {
            return "降雨";
        } else if (5 == weather.intValue()) {
            return "风力";
        }
        return "";
    }

    /**
     * 获取天气单位
     * 
     * @author Lxl
     * @param weather
     * @return
     */
    public static String getUnit(Byte weather) {
        if (1 == weather.intValue()) {
            return "℃";
        } else if (2 == weather.intValue()) {
            return "℃";
        } else if (3 == weather.intValue()) {
            return "℃";
        } else if (4 == weather.intValue()) {
            return "mm";
        } else if (5 == weather.intValue()) {
            return "级";
        }
        return "";
    }

    /**
     * <10mm时，加：(小雨) 10-25mm时，加：(中雨) 25-50mm时，加：(大雨) 50-100mm时，加：(暴雨) 100-250mm时，加：(大暴雨) >250mm时，加：(特大暴雨)
     * 
     * @param threshold
     * @return
     */
    public static String getRainLevel(float threshold, boolean needTips) {
        String tips = "";
        if (threshold < 10) {
            if (needTips) {
                tips = "，出门不需打伞，洼地积水慢";
            }
            return "(小雨" + tips + ")";
        } else if (threshold >= 10 && threshold < 25) {
            if (needTips) {
                tips = "，出门需打伞，雨落硬地四溅";
            }
            return "(中雨" + tips + ")";
        } else if (threshold >= 25 && threshold < 50) {
            if (needTips) {
                tips = "，即便打伞也会全身透湿";
            }
            return "(大雨" + tips + ")";
        } else if (threshold >= 50) {
            if (needTips) {
                tips = "，即便打伞也会全身透湿";
            }
            return "(暴雨" + tips + ")";
        }
        return "";
    }

    // public static String getStation(CityInfo cityInfo) {
    // String station = "以{0}气象站（编号{1}）为准";
    // return MessageFormat.format(station, cityInfo.getCnName(), cityInfo.getCityId());
    // }
}
