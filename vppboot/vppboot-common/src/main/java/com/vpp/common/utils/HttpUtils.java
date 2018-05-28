package com.vpp.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * http请求工具类
 * 
 * @author Lxl
 * @version V1.0 2017年12月5日
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final int maxTotalConnect = 0; // 连接池的最大连接数默认为0
    private static final int maxConnectPerRoute = 2000; // 单个主机的最大连接数
    private static final int connectTimeout = 3000; // 连接超时默认2s
    private static final int readTimeout = 30000; // 读取超时默认30s

    // 创建HTTP客户端工厂
    private static ClientHttpRequestFactory createFactory() {
        if (maxTotalConnect <= 0) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(connectTimeout);
            factory.setReadTimeout(readTimeout);
            return factory;
        }
        @SuppressWarnings("unused")
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(maxTotalConnect)
                .setMaxConnPerRoute(maxConnectPerRoute).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        return factory;
    }

    public static String post(String url, Map<String, Object> params) {
        RestTemplate restTemplate = new RestTemplate(createFactory());
        // 设置字符集UTF-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);// 表单形式提交
            headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
            headers.add(HttpHeaders.CONNECTION, "keep-alive");

            // 封装参数
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
            param.setAll(params);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(param, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                return response.getBody();
            } else {
                logger.error("httpPost error :::{}", response.getBody());
            }
        } catch (Exception e) {
            logger.error("httpPost error:::{}", e.getMessage());
        }
        return null;
    }
    
    public static String postString(String url, Map<String, String> params) {
        RestTemplate restTemplate = new RestTemplate(createFactory());
        // 设置字符集UTF-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);// 表单形式提交
            headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
            headers.add(HttpHeaders.CONNECTION, "keep-alive");

            // 封装参数
            MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
            param.setAll(params);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                return response.getBody();
            } else {
                logger.error("httpPost error :::{}", response.getBody());
            }
        } catch (Exception e) {
            logger.error("httpPost error:::{}", e.getMessage());
        }
        return null;
    }

    public static String post(String url, JSONObject params) {
        RestTemplate restTemplate = new RestTemplate(createFactory());
        // 设置字符集UTF-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);// 表单形式提交
            headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN");
            headers.add(HttpHeaders.CONNECTION, "keep-alive");

            HttpEntity<JSONObject> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                return response.getBody();
            } else {
                logger.error("httpPost error :::{}", response.getBody());
            }
        } catch (Exception e) {
            logger.error("httpPost error:::{}", e.getMessage());
        }
        return null;
    }

    public static String get(String url, Map<String, String> params) {
        RestTemplate restTemplate = new RestTemplate(createFactory());
        // 设置字符集UTF-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        try {
            // 封装参数
            StringBuilder paramStr = new StringBuilder();
            paramStr.append("?");
            for (String key : params.keySet()) {
                paramStr.append(key).append("=").append(params.get(key)).append("&");
            }
            url = url + paramStr.toString().substring(0, paramStr.length() - 1);
            // 封装参数
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                return response.getBody();
            } else {
                logger.error("httpGet error :::{}", response.getBody());
            }
        } catch (Exception e) {
            logger.error("httpGet error:::{}", e.getMessage());
        }
        return null;
    }
}
