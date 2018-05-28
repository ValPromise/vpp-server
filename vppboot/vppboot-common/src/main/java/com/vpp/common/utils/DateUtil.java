package com.vpp.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

/**
 * <p>
 * Title: 时间操作工具
 * </p>
 * <p>
 * Description: SOC基础技术平台
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author sbruan
 * @version 1.0
 */
public class DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String LONG_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String LONG_DATE_TIME_PATTERN_SSS = "yyyyMMddHHmmssSSS";

    /**
     * yyyyMMdd
     */
    public static final String YMD_DATE_TIME_PATTERN = "yyyyMMdd";

    /**
     * yyyy-MM-dd
     */
    public static final String YMD_DATE_PATTERN = "yyyy-MM-dd";


    /**
     * yyyy-MM
     */
    public static final String YM_DATE_PATTERN = "yyyy-MM";


    private static final String S = ".0";

    public static String formatDate(java.util.Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }
    
    public static String formatDateTime(java.util.Date date) {
        return formatDateByFormat(date, LONG_DATE_TIME_PATTERN);
    }

    public static String formatDateByFormat(java.util.Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String formatDateByFormatHaoMiao() {
        SimpleDateFormat formatter = new SimpleDateFormat(LONG_DATE_TIME_PATTERN_SSS);
        String formatStr = formatter.format(new Date());
        return formatStr;
    }

    public static java.util.Date parseYMDDate(int ymdDate) {
        return parseDate(String.valueOf(ymdDate), YMD_DATE_TIME_PATTERN);
    }

    public static java.util.Date parseDate(java.sql.Date date) {
        return date;
    }

    public static Date parseDate(Date date, String format) {
        SimpleDateFormat sdff = new SimpleDateFormat(format);
        try {
            return sdff.parse(sdff.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.sql.Date parseSqlDate(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        } else {
            return null;
        }
    }

    public static String format(java.util.Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                java.text.DateFormat df = new java.text.SimpleDateFormat(format);
                result = df.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String format(java.util.Date date) {
        return format(date, "yyyy/MM/dd");
    }

    public static String format1(java.util.Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static int getYear(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.YEAR);
    }

    public static int getMonth(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MONTH) + 1;
    }

    public static int getDay(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.DAY_OF_MONTH);
    }

    public static int getHour(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MINUTE);
    }

    public static int getSecond(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.SECOND);
    }

    public static long getMillis(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    public static int getWeek(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    public static String getDate(java.util.Date date) {
        return format(date, "yyyy/MM/dd");
    }

    public static String getDate(java.util.Date date, String formatStr) {
        return format(date, formatStr);
    }

    public static String getTime(java.util.Date date) {
        return format(date, "HH:mm:ss");
    }

    public static String getDateTime(java.util.Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期相加
     * 
     * @param date Date
     * @param day int
     * @return Date
     */
    public static java.util.Date addDate(java.util.Date date, int day) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相加
     * 
     * @param date Date
     * @param day int
     * @return Date
     */
    public static String addDate(String date, int day) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        Date d = parseDate(date);
        c.setTime(d);
        c.add(Calendar.DAY_OF_MONTH, day);
        return format1(c.getTime());
    }

    /**
     * 日期天数相减
     * 
     * @param date Date
     * @param day int
     * @return Date
     */
    public static java.util.Date diffDate(java.util.Date date, int day) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 小时相加
     * 
     * @param date Date
     * @param hour int
     * @return Date
     */
    public static java.util.Date addHour(java.util.Date date, int hour) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) hour) * 1 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 小时相加
     * 
     * @param date Date
     * @param hour int
     * @return Date
     */
    public static java.util.Date addYear(java.util.Date date, int year) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date));
        return c.getTime();
    }

    /**
     * 减second秒
     * 
     * @param date
     * @param second
     * @return
     */
    public static Date diffSecond(java.util.Date date, int second) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) - ((long) second) * 1000);
        return c.getTime();
    }

    /**
     * 通过当前日期获取相差 day 天前的时间 时分秒00:00:00
     * 
     * @param time
     * @param day
     * @return
     */
    public static String getStartTime3(Date time, int day) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time);
        long millSeconds1 = calendar1.getTimeInMillis();
        long tmpDays = Long.valueOf(String.valueOf(day)) * (24 * 60 * 60 * 1000);
        return DateUtil.format(new Date(millSeconds1 - tmpDays), "yyyy-MM-dd") + " 00:00:00";
    }

    /**
     * 日期相减
     * 
     * @param date Date
     * @param date1 Date
     * @return int
     */
    public static int diffDate(java.util.Date date, java.util.Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 日期相减(返回秒值)
     * 
     * @param date Date
     * @param date1 Date
     * @return int
     * @author
     */
    public static Long diffDateTime(java.util.Date date, java.util.Date date1) {
        return (Long) ((getMillis(date) - getMillis(date1)) / 1000);
    }

    public static java.util.Date getdate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    public static java.util.Date getdate1(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(date);
    }

    public static java.util.Date getMaxTimeByStringDate(String date) throws Exception {
        String maxTime = date + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(maxTime);
    }

    public static java.util.Date getMinTimeByStringDate(String date) throws Exception {
        String maxTime = date + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(maxTime);
    }

    public static java.util.Date stringToDate(String date, String dataFormat) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
        return sdf.parse(date);
    }

    public static String stringToString(String date, String dataFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
        try {
            return sdf.format(sdf.parse(date));
        } catch (ParseException e) {

        }
        return null;
    }

    public static String stringToString(String sourceDate, String sourceFormat, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat);
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.format(sdf.parse(sourceDate));
        } catch (ParseException e) {

        }
        return null;
    }

    /**
     * 获得当前时间
     * 
     * @return
     */
    public static Date getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = DateUtil.getDateTime(date);
        try {
            return sdf.parse(result);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDateTimeToStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(getCurrentDateTime());
    }

    public static String getCurrentDateTimeString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(getCurrentDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDateTimeLocal() {
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_TIME_PATTERN);
        try {
            return sdf.format(getCurrentDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getWmsupdateDateTime() {
        Calendar cl = Calendar.getInstance();

        return cl.getTimeInMillis();
    }

    // TODO Everyone don't modify wms team the code

    /****************************** start by wms ******************************/

    public static final Date parseDate(String string) {

        return parseDate(string, DateTimePattern.values());
    }

    public static final Date parseDate(String string, DateTimePattern pattern) {

        return parseDate(string, new DateTimePattern[] { pattern });
    }

    /**
     * 解析日期时间字符串，得到Date对象
     * 
     * @param input 日期时间字符串
     * @param pattern 格式
     * @return
     */
    public static Date parseDate(String input, String pattern) {
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
            return simpledateformat.parse(input);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static final Date parseDate(String string, DateTimePattern[] patterns) {

        Date date = null;

        try {
            String[] datetimePattern = new String[patterns.length];

            for (int i = 0; i < datetimePattern.length; i++) {

                datetimePattern[i] = patterns[i].getPattern();
            }

            date = DateUtils.parseDate(string, datetimePattern);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return date;
    }

    public static final String formatDate(Date date, DateTimePattern pattern) {

        String string = null;

        try {
            string = DateFormatUtils.format(date, pattern.getPattern());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return string;
    }

    public enum DateTimePattern {

        ISO_DATE("yyyy-MM-dd"), ISO_DATETIME_NO_SECOND("yyyy-MM-dd HH:mm"), ISO_DATETIME("yyyy-MM-dd HH:mm:ss"), ISO_DATETIME_HAS_MILLISECOND(
                "yyyy-MM-dd HH:mm:ss.SSS");

        private String pattern;

        private DateTimePattern(String pattern) {

            this.pattern = pattern;
        }

        public String getPattern() {

            return this.pattern;
        }
    }

    public static Date formatDateToStartDateTime(String strDate) {
        java.util.Date date = null;
        try {
            date = convertStringToDate(LONG_DATE_TIME_PATTERN, strDate + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * This method generates a string representation of a date/time in the format you specify on input
     * 
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            // log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(String strDate) throws ParseException {

        if (strDate == null || strDate.equals("")) {
            return null;
        }

        Date aDate = null;

        try {
            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return aDate;
    }

    /**
     * Return default datePattern (MM/dd/yyyy)
     * 
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return "yyyy-MM-dd";
    }

    public static Date formatDateToEndDateTime(String strDate) {
        java.util.Date date = null;
        try {
            date = convertStringToDate(LONG_DATE_TIME_PATTERN, strDate + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * This method generates a string representation of a date based on the System Property 'dateFormat' in the format you
     * specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method generates a string representation of a date's date/time in the format you specify on input
     * 
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 根据当前日期获取后一天的日期
     * 
     * @return
     * @throws ParseException
     */
    public static String getAfterDateByNow(String nowDate) throws ParseException {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = sdf.parse(nowDate);
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        str = sdf.format(cal.getTime());
        return str;
    }

    /**
     * 根据当前日期获取前一天的日期
     * 
     * @return
     * @throws ParseException
     */
    public static String getBeforeDateByNow(String nowDate) throws ParseException {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = sdf.parse(nowDate);
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        str = sdf.format(cal.getTime());
        return str;
    }

    /**
     * 获取年月之间相隔多少月
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthNum(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal1.get(1) - cal2.get(1)) * 12 + (cal1.get(2) - cal2.get(2));

    }

    /**
     * 将传过来的日期yyyy-MM-dd HH:mm:ss 转换为 yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return
     */
    public static Date formatDateToStartDateTime2(String strDate) {
        java.util.Date date = null;
        try {
            date = convertStringToDate(LONG_DATE_TIME_PATTERN, strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期相减(返回分钟)
     * 
     * @param date Date
     * @param date1 Date
     * @return int
     * @author
     */
    public static Long diffDateTimeMin(java.util.Date date, java.util.Date date1) {
        return (Long) ((getMillis(date) - getMillis(date1)) / (60 * 1000));
    }

    /**
     * 获取系统的 Timestamp
     * 
     * @return 系统当前时间的 Timestamp
     */
    public static Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 给指定日期加几个月
     * 
     * @param date 指定的日期
     * @param numMonths 需要往后加的月数
     * @return 加好后的日期
     */
    public static Date addMonthsToDate(Date date, int numMonths) {
        Assert.notNull(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, numMonths);
        return c.getTime();
    }

    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " HH:mm:ss.S";
    }

    /**
     * 将一个指定的日期格式化成指定的格式
     * 
     * @param date 指定的日期
     * @param pattern 指定的格式
     * @return 格式化好后的日期字符串
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return date;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public static Date parseShortDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(date, new String[] { "yyyy-MM-dd" });
        } catch (ParseException e) {
            // e.printStackTrace();
        }
        return null;
    }

    public static String getAddOrSubData(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);// 让日期减1
        return formatDate(calendar.getTime(), "yyyy-MM");
    }

    /**
     * 获取开始时间
     * 
     * @param month
     * @return
     */
    public static String getbTime(String month) {
        StringBuffer str = new StringBuffer();
        str.append(month).append("-" + "26");
        Date bTime = parseDate(str.toString(), "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bTime);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);// 让日期减1
        return formatDate(calendar.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取结束时间
     * 
     * @param month
     * @return
     */
    public static String geteTime(String month) {
        StringBuffer str = new StringBuffer();
        str.append(month).append("-" + "26");
        return str.toString();
    }

    /**
     * 获取初期日期
     * 
     * @param month
     * @return
     */
    public static String getEarlyDate(String month) {
        StringBuffer str = new StringBuffer();
        str.append(month).append("-" + "26");
        Date bTime = parseDate(str.toString(), "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bTime);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);// 让日期减1
        return formatDate(calendar.getTime(), "yyyy-MM-dd");
    }

    /**
     * 期末
     * 
     * @param month
     * @return
     */
    public static String getFinalDate(String month) {
        StringBuffer str = new StringBuffer();
        str.append(month).append("-" + "26");
        return str.toString();
    }

    /**
     * @Title: strToStr
     * @Description: 将string日期转成另外一个string日期
     * @param strTime 转换的日期
     * @param originalFormat 原格式
     * @param resultFormat 转换后的格式
     * @throws Exception 异常
     * @return String 返回类型
     */
    public static String strToStr(String strTime, String originalFormat, String resultFormat) throws Exception {
        Date date = stringToDate(strTime, originalFormat);
        return formatDate(date, resultFormat);
    }

    public static Date strToOtherDate(String strTime, String originalFormat, String resultFormat) throws Exception {
        String date = strToStr(strTime, originalFormat, resultFormat);
        return parseDate(date, resultFormat);
    }

    public static int getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static String StrTimeToMax(String strTime) {
        String[] dateArray = strTime.split(",");
        return showResult(dateArray, "max");
    }

    public static String StrTimeToMin(String strTime) {
        String[] dateArray = strTime.split(",");
        return showResult(dateArray, "min");
    }

    public static String showResult(String[] dateArray, String sta) {
        Map<String, Integer> dateMap = new TreeMap<String, Integer>();
        int i, arrayLen;
        arrayLen = dateArray.length;
        for (i = 0; i < arrayLen; i++) {
            String dateKey = dateArray[i];
            if (dateMap.containsKey(dateKey)) {
                int value = dateMap.get(dateKey) + 1;
                dateMap.put(dateKey, value);
            } else {
                dateMap.put(dateKey, 1);
            }
        }
        Set<String> keySet = dateMap.keySet();
        String[] sorttedArray = new String[keySet.size()];
        Iterator<String> iter = keySet.iterator();
        int index = 0;
        while (iter.hasNext()) {
            String key = iter.next();
            sorttedArray[index++] = key;
        }
        int sorttedArrayLen = sorttedArray.length;
        if (sta.contains("min")) {
            return sorttedArray[0];
        } else {
            return sorttedArray[sorttedArrayLen - 1];
        }
        // System.out.println("最小日期是：" + sorttedArray[0] + "," +
        // " 天数为" + dateMap.get(sorttedArray[0]));
        // System.out.println("最大日期是：" + sorttedArray[sorttedArrayLen - 1] + ","
        // +
        // " 天数为" + dateMap.get(sorttedArray[sorttedArrayLen - 1]));
    }

    /**
     * 获取今天
     * 
     * @return
     */
    public static Date convertTimeToDate(Date date) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        String sDate = sm.format(date);
        Date d = null;
        try {
            d = sm.parse(sDate);
        } catch (ParseException e) {
        }

        return d;
    }

    private static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static String getYesterdayString(String format) {
        String date = getStringByFormat(getYesterday(), format);
        return date;
    }

    public static String getYesterdayString(Date today, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        String date = format(cal.getTime(), format);
        return date;
    }

    /**
     * 获取前?个小时 TODO 添加方法注释
     * 
     * @author lxy
     * @param today
     * @param format
     * @param hour
     * @return
     */
    public static String getbfHour(Date today, String format, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.HOUR, hour);
        String date = format(cal.getTime(), format);
        return date;
    }

    /**
     * 获取明天时间字符串
     * 
     * @author Lxl
     * @param format
     * @return
     */
    public static String getTomorrowString(String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String date = getStringByFormat(cal.getTime(), format);
        return date;
    }

    /**
     * 根据当前时间获取明天时间字符串
     * 
     * @author Lxl
     * @param today
     * @param format
     * @return
     */
    public static String getTomorrowString(Date today, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String date = getStringByFormat(cal.getTime(), format);
        return date;
    }

    public static Date getYesterdayDate(String format) {
        Date date = getDateByFormat(getYesterday(), format);
        return date;
    }

    public static Date getDateByFormat(Date date, String format) {
        String dateString = getStringByFormat(date, format);
        Date resultDate = null;
        try {
            SimpleDateFormat sm = new SimpleDateFormat(format);
            resultDate = sm.parse(dateString);
        } catch (ParseException e) {
        }
        return resultDate;
    }

    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat sm = new SimpleDateFormat(format);
        String dateString = sm.format(date);
        return dateString;
    }

    public static Date parse(String dateString, String format) {
        Date resultDate = null;
        try {
            SimpleDateFormat sm = new SimpleDateFormat(format);
            resultDate = sm.parse(dateString);
        } catch (ParseException e) {
        }
        return resultDate;
    }

    public static String getYesterMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String time = format.format(c.getTime());
        return time;
    }

    /**
     * 格式化开始日期，如20160723格式化为2016-07-23 00:00
     * 
     * @param stime
     * @return
     */
    public static String formatStime(String etimeString) {
        Date etime = parse(etimeString, DateUtil.LONG_DATE_TIME_PATTERN);
        SimpleDateFormat sm = new SimpleDateFormat(DateUtil.YMD_DATE_PATTERN);
        String st = sm.format(etime) + " 00:00";
        return st;
    }

    /**
     * 格式化结束日期，如20160701格式化为2016-06-30 23:59
     * 
     * @param etime
     * @return
     */
    public static String formatEtime(String etimeString) {
        Date etime = parse(etimeString, DateUtil.LONG_DATE_TIME_PATTERN);
        Calendar cal = Calendar.getInstance();
        cal.setTime(etime);
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sm = new SimpleDateFormat(DateUtil.YMD_DATE_PATTERN);
        String st = sm.format(cal.getTime()) + " 23:59";
        return st;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合，不包括结束时间
     * 
     * @param beginDate 20170518
     * @param endDate 20170520
     * @return 20170518,20170519
     */
    public static List<String> getDatesBetweenTwoDate(Date beginDate, Date endDate, String format) {
        SimpleDateFormat sm = new SimpleDateFormat(format);
        // Date beginDate = null;
        // Date endDate = null;
        // try {
        // beginDate = sm.parse(beginDate);
        // endDate = sm.parse(endDate);
        // } catch (ParseException e) {
        // return null;
        // }
        if (null == beginDate || null == endDate) {
            return null;
        }
        List<String> lDate = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();

        lDate.add(sm.format(beginDate));
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (endDate.after(cal.getTime())) {
                lDate.add(sm.format(cal.getTime()));
            } else {
                break;
            }
        }
        return lDate;
    }

    /**
     * 查询两个日期相差天数
     * 
     * @author Lxl
     * @param sDate 开始日期
     * @param eDate 结束日期
     * @return
     */
    public static int betweenOfDays(Date sDate, Date eDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(sDate);
        long am = aCalendar.getTimeInMillis();

        aCalendar.setTime(eDate);
        long em = aCalendar.getTimeInMillis();
        long m = em - am;
        return (int) (m / (1000 * 60 * 60 * 24));
    }

    /**
     * 获取最小时间
     * 
     * @author Lxl
     * @param dateList
     * @return
     */
    public static Date min(List<Date> dateList) {
        Date minDate = dateList.get(0);
        for (Date dt : dateList) {
            if (dt.before(minDate)) {
                minDate = dt;
            }
        }
        return minDate;
    }

    /**
     * 获取最大时间
     * 
     * @author Lxl
     * @param dateList
     * @return
     */
    public static Date max(List<Date> dateList) {
        Date minDate = dateList.get(0);
        for (Date dt : dateList) {
            if (dt.after(minDate)) {
                minDate = dt;
            }
        }
        return minDate;
    }

    // 检查一个字符串的时期是不是连续 不连续 返回false
    public static boolean CheckDateForContinuity(String times) {
        String[] timesArr = times.split(",");
        for (int i = timesArr.length - 1; i > 0; i--) {
            int days = DateUtil.diffDate(DateUtil.parseDate(timesArr[i]), DateUtil.parseDate(timesArr[i - 1]));
            if (days > 1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date sdate = new Date();
        Date edate = cal.getTime();
//        System.out.println(sdate.toLocaleString());
//        System.out.println(edate.toLocaleString());
//        System.out.println(betweenOfDays(sdate, edate));
//
//        String edd = "2017-11-28 19:24:01";
//        System.out.println(betweenOfDays(sdate, parse(edd, LONG_DATE_TIME_PATTERN)));
//        
//        System.out.println(removeS("2017-11-27 17:08:32"));
        
        
       String newDate = addDate(cal.getTime(), 10).toLocaleString();
       System.out.println(newDate);
        // String ss = "2017-08-12  00:00:00";
        // System.out.println();
        // List<Date> dateList = new ArrayList<>();
        // dateList.add(cal.getTime());
        // System.out.println(cal.getTime().toLocaleString());
        // cal.add(Calendar.DAY_OF_MONTH, 2);
        // dateList.add(cal.getTime());
        // System.out.println(cal.getTime().toLocaleString());
        // cal.add(Calendar.DAY_OF_MONTH, 2);
        // dateList.add(cal.getTime());
        // System.out.println(cal.getTime().toLocaleString());
        // System.out.println(min(dateList).toLocaleString());
        // System.out.println(max(dateList).toLocaleString());
    }

    /**
     * 格式化开始日期，如20160723格式化为2016-07-23 00:00
     * 
     * @param stime
     * @return
     */
    public static String formatSearchEtime(String etimeString) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String st = "";
        try {
            Date date = (Date) formatter.parse(etimeString);
            etimeString.equals(formatter.format(date));
            st = etimeString + " 23:59:59";
        } catch (Exception e) {
            st = etimeString;
        }
        return st;
    }

    /**
     * 移除mysql时间末尾.0
     * 
     * @author Lxl
     * @param dateString
     * @return
     */
    public static String removeS(String dateString) {
        if (null != dateString && dateString.contains(S)) {
            return dateString.replace(S, "");
        }
        return dateString;
    }
}
