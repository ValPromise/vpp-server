package com.vpp.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpSenderUtils {

    private URLConnection connection;
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * @Title: HttpSenderUtils
     * @Description: HttpSenderUtils构造函数,设置默认请求头
     * @param url 初始化地址
     * @date 2016年9月18日 下午6:37:46
     */
    public HttpSenderUtils(String url) throws Exception {
        this(url, true);
    }

    /**
     * @Title: HttpSenderUtils
     * @Description: HttpSenderUtils构造函数
     * @param url 初始化地址
     * @param setCommonHeaders 是否设置默认请求头
     * @date 2016年9月18日 下午6:37:46
     */
    public HttpSenderUtils(String url, boolean setCommonHeaders) throws Exception {
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        connection = realUrl.openConnection();
        if (setCommonHeaders) {
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        }
    }

    /**
     * @Title: appendHeaders
     * @Description: 发送get请求
     * @param headerKey 请求头key
     * @param headerValue 请求头value
     * @return HttpSenderUtils 当前对象
     * @date 2016年9月18日 下午6:37:46
     */
    public HttpSenderUtils addHeaders(String headerKey, String headerValue) {
        connection.setRequestProperty(headerKey, headerValue);
        return this;
    }

    /**
     * @Title: sendGetRequest
     * @Description: 发送get请求
     * @throws IOException
     * @return String 请求返回值
     * @date 2016年9月18日 下午6:37:46
     */
    public String sendGetRequest() throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        BufferedReader bufferedReader = null; // 接受连接受的参数
        connection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }

    /**
     * @Title: sendGetRequest
     * @Description: 发送get请求
     * @param url 请求地址
     * @throws IOException
     * @return String 请求返回值
     * @date 2016年9月18日 下午6:37:46
     */
    public static String sendGetRequest(String url) throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        URL httpUrl = null; // HTTP URL类 用这个类来创建连接
        URLConnection urlConnection = null; // 创建的http连接
        BufferedReader bufferedReader = null; // 接受连接受的参数
        // 创建URL
        httpUrl = new URL(url);
        // 建立连接
        urlConnection = httpUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "keep-alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        urlConnection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }

    public static String sendGetRequestIgnoreException(String url) {
        return sendGetRequestIgnoreException(url, true);
    }

    public static String sendGetRequestIgnoreException(String url, boolean print) {
        String result = null;
        try {
            result = sendGetRequest(url);
        } catch (IOException e) {
            if (print) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @Title: sendGetRequest
     * @Description: 发送get请求
     * @param url 请求地址
     * @throws IOException
     * @return String 请求返回值
     * @date 2016年9月18日 下午6:37:46
     */
    public static List<String> sendGetRequestGetArray(String url) throws IOException {
        List<String> result = new ArrayList<String>(); // 用来接受返回值
        URL httpUrl = null; // HTTP URL类 用这个类来创建连接
        URLConnection urlConnection = null; // 创建的http连接
        BufferedReader bufferedReader = null; // 接受连接受的参数
        // 创建URL
        httpUrl = new URL(url);
        // 建立连接
        urlConnection = httpUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "keep-alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        urlConnection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.add(line);
        }
        bufferedReader.close();
        return result;
    }

    /**
     * @Title: sendPostRequest
     * @Description: 发送post请求
     * @param parameters 请求参数
     * @throws IOException
     * @return String 请求返回值
     * @date 2016年9月18日 下午6:37:46
     */
    public String sendPostRequest(String parameters) throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        BufferedReader bufferedReader; // 接受连接受的参数
        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);
        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
        printWriter.print(parameters);
        printWriter.flush();
        connection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        printWriter.close();
        bufferedReader.close();
        return result.toString();
    }

    /**
     * @Title: sendPostRequest
     * @Description: 发送post请求
     * @param url 请求地址
     * @param parameters 请求参数
     * @throws IOException
     * @return String 请求返回值
     * @date 2016年9月18日 下午6:37:46
     */
    public static String sendPostRequest(String url, String parameters) throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        URL httpUrl = null; // HTTP URL类 用这个类来创建连接
        URLConnection urlConnection = null; // 创建的http连接
        PrintWriter printWriter = null;
        BufferedReader bufferedReader; // 接受连接受的参数
        // 创建URL
        httpUrl = new URL(url);
        // 建立连接
        urlConnection = httpUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "keep-alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        urlConnection.setRequestProperty("Charset", "UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(9000);
        urlConnection.setReadTimeout(9000);
        printWriter = new PrintWriter(urlConnection.getOutputStream());
        printWriter.print(parameters);
        printWriter.flush();
        urlConnection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        printWriter.close();
        bufferedReader.close();
        return result.toString();
    }

    public static String sendPostMap(String url, Map<String, Object> parameters) throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        URL httpUrl = null; // HTTP URL类 用这个类来创建连接
        URLConnection urlConnection = null; // 创建的http连接
        PrintWriter printWriter = null;
        BufferedReader bufferedReader; // 接受连接受的参数
        // 创建URL
        httpUrl = new URL(url);
        // 建立连接
        urlConnection = httpUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "keep-alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        urlConnection.setRequestProperty("Charset", "UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(9000);
        urlConnection.setReadTimeout(9000);
        printWriter = new PrintWriter(urlConnection.getOutputStream());

        StringBuilder params = new StringBuilder();
        for (String key : parameters.keySet()) {
            params.append(key).append("=").append(parameters.get(key)).append("&");
        }
        printWriter.print(params.substring(0, params.length() - 1).toString());
        printWriter.flush();
        urlConnection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        printWriter.close();
        bufferedReader.close();
        return result.toString();
    }

    /**
     * @Title: sendPostRequest
     * @Description: 发送post请求
     * @param url 请求地址
     * @param parameters 请求参数
     * @throws IOException
     * @return String 请求返回值
     * @date 2016年9月18日 下午6:37:46
     */
    public static String sendPostRequestTimeout(String url, String parameters, Integer timeout) throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        URL httpUrl = null; // HTTP URL类 用这个类来创建连接
        URLConnection urlConnection = null; // 创建的http连接
        PrintWriter printWriter = null;
        BufferedReader bufferedReader; // 接受连接受的参数
        // 创建URL
        httpUrl = new URL(url);
        // 建立连接
        urlConnection = httpUrl.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "keep-alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        urlConnection.setRequestProperty("Charset", "UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(timeout);
        urlConnection.setReadTimeout(timeout);
        printWriter = new PrintWriter(urlConnection.getOutputStream());
        printWriter.print(parameters);
        printWriter.flush();
        urlConnection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        printWriter.close();
        bufferedReader.close();
        return result.toString();
    }

    /**
     * 设置https post请求
     **/

    public static String sendHttpsPostRequest(String url, String parameters) throws IOException {
        StringBuffer result = new StringBuffer(); // 用来接受返回值
        URL httpUrl = null; // HTTP URL类 用这个类来创建连接
        URLConnection urlConnection = null; // 创建的http连接
        PrintWriter printWriter = null;
        BufferedReader bufferedReader; // 接受连接受的参数
        trustAllHosts();
        // 创建URL
        httpUrl = new URL(url);
        // 建立连接
        HttpsURLConnection https = (HttpsURLConnection) httpUrl.openConnection();
        if (httpUrl.getProtocol().toLowerCase().equals("https")) {
            https.setHostnameVerifier(DO_NOT_VERIFY);
            urlConnection = https;
        } else {
            urlConnection = (HttpURLConnection) httpUrl.openConnection();
        }

        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "keep-alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        printWriter = new PrintWriter(urlConnection.getOutputStream());
        printWriter.print(parameters);
        printWriter.flush();
        urlConnection.connect();
        // 接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        printWriter.close();
        bufferedReader.close();
        return result.toString();
    }

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
