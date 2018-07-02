package com.vpp.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权工具类，加密规则：request中所有非空参数， 把参数数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串 +SECURITY_KEY，生成MD5加密字符串 存入request中sign字段
 * 
 * @author Lxl
 * @version V1.0 2017年12月20日
 */
public class SecurityUtils {

    /**
     * 鉴权KEY，所有子系统同步
     */
    public static final String SECURITY_KEY = "tqb_201712_0FA9B4DE5B83AD3A34860984C0D9EA1A";

    private static final String charset = "UTF-8";

    /**
     * 根据Map<String, String>生成签名
     * 
     * @author Lxl
     * @param request
     * @return
     */
    public static String getSign(Map<String, String> params) {
        // 过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(params);
        // 获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        String signStr = sign(preSignStr, SECURITY_KEY);
        return signStr;
    }

    /**
     * 根据request生成签名
     * 
     * @author Lxl
     * @param request
     * @return
     */
    public static String getSignByRequest(HttpServletRequest request) {
        // 获取request中所有参数
        Map<String, String> params = getMapByRequest(request);
        // 过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(params);
        // 获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        String signStr = sign(preSignStr, SECURITY_KEY);
        return signStr;
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * 
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    public static boolean checkSignVeryfy(HttpServletRequest request, String sign) {
        // 获取request中所有参数
        Map<String, String> params = getMapByRequest(request);
        // 过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(params);
        // 获取待签名字符串
        String preSignStr = createLinkString(sParaNew);

        // 获得签名验证结果
        boolean isSign = verify(preSignStr, sign, SECURITY_KEY);
        return isSign;
    }

    /**
     * VPP2.0加密验证
     * 
     * @author Lxl
     * @param request
     * @return
     */
    public static boolean checkVppAppSignVeryfy(HttpServletRequest request) {
        // 验证time加密盐
        String key = request.getParameter("key");
        String _key = request.getParameter("_key");
//        String local = DateUtil.format(new Date(), DateUtil.YMDH_DATE_PATTERN);
        Map<String, String> params = new HashMap<String, String>();
        params.put("time", _key);
        if (StringUtils.isNotBlank(key)) {
            String sign = getSign(params);
            if (!sign.equals(key)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 除去数组中的空值和签名参数
     * 
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * 
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 获取request中参数，用于验证签名
     * 
     * @author Lxl
     * @param request
     * @return
     */
    private static Map<String, String> getMapByRequest(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<?, ?> requestParams = request.getParameterMap();
        for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 签名字符串
     * 
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    private static String sign(String text, String key) {
        text = text + key;
        return MD5Utils.getMD5String(getContentBytes(text));
    }

    /**
     * 签名字符串
     * 
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    private static boolean verify(String text, String sign, String key) {
        text = text + key;
        String mysign = MD5Utils.getMD5String(getContentBytes(text));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content) {
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
