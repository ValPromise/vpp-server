package com.vpp.common.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import com.vpp.common.utils.Constants;

//@Order(1)
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
public class SessionFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(SessionFilter.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String IP_HASH = "ip_hash";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String[] notFilter = new String[] {};// 不过滤的uri

        String uri = request.getRequestURI();// 请求的uri

//        String ip = this.getRemoteAddress(request);
//        if (null == ip) {
////            this.printError(response);
//        }
//        logger.info("RemoteAddress ::: {} ", ip);

        // logger.info("url:::{}", uri);
        Map<String, String> requestMap = getMapByRequest(request);
        // logger.info("params:::{}", requestMap);
        // uri中包含background时才进行过滤
        // if (uri.indexOf("/message/") != -1 || uri.indexOf("/trigger/") != -1 || uri.indexOf("/tqbjob/") != -1) {
        // String sign = request.getParameter("sign");
        // boolean isSign = SecurityUtils.checkSignVeryfy(request, sign);
        // if (!isSign) {
        // request.setCharacterEncoding("UTF-8");
        // response.setCharacterEncoding("UTF-8");
        // PrintWriter out = response.getWriter();
        // out.print("...---=== U-n-a-u-t-h-o-r-i-z-e-d ===---...");
        // } else {
        // filterChain.doFilter(request, response);
        // }
        // logger.info("filter");
        // } else {
        // logger.info("no filter");
        // filterChain.doFilter(request, response);
        // }
        filterChain.doFilter(request, response);
    }

    private String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();

        for (String ipTemp : ip.split(",")) {
//            if(null != ipTemp && ipTemp.equals("175.168.31.231")){
//                logger.info("Blacklist to ::: {}", ipTemp);
//                return null;
//            }
            logger.info("redisTemplate ::: {},{}", ipTemp,redisTemplate);
            if(null != ipTemp && null != redisTemplate){
                System.out.println("-----------"+redisTemplate);
                if (redisTemplate.hasKey(IP_HASH + ipTemp)) {
                    Integer count = Integer.parseInt(""+redisTemplate.opsForValue().get(IP_HASH + ipTemp));
                    if (count.intValue() > 20) {
                        logger.info("Blacklist to ::: {}", ipTemp);
                        redisTemplate.opsForValue().set(IP_HASH + ipTemp, ""+(count.intValue() + 1), Constants.CACHE_TIME_MINUTE, TimeUnit.SECONDS);
                        
                        return null;
                    }
                    redisTemplate.opsForValue().set(IP_HASH + ipTemp, ""+(count.intValue() + 1), Constants.CACHE_TIME_MINUTE, TimeUnit.SECONDS);
     
                    // redis.opsForHash().put(IP_HASH, ipTemp, 1);
                } else {
                    redisTemplate.opsForValue().set(IP_HASH + ipTemp, ""+1, Constants.CACHE_TIME_MINUTE, TimeUnit.SECONDS);
                    // redis.opsForHash().put(IP_HASH, ipTemp, count.intValue() + 1);
                }
            }
        }
        return ip;
    }

    private void printError(HttpServletResponse response) {
        // ------------------out.print 参数设置 start--------
        // 设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        // 允许的访问方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "token,Origin, X-Requested-With, Content-Type, Accept");
        response.setContentType("application/json;charset=UTF-8");
        // ------------------out.print 参数设置 end--------

        PrintWriter writer = null;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            writer = new PrintWriter(osw, true);
            writer.write("...---=== U-n-a-u-t-h-o-r-i-z-e-d ===---...");
            writer.flush();
            writer.close();
            osw.close();
        } catch (UnsupportedEncodingException e) {
            logger.error("过滤器返回信息失败:" + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("过滤器返回信息失败:" + e.getMessage(), e);
        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                }
            }
        }
        return;
    }

    public static void main(String[] args) {
        System.out.println("123" + null);
    }

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
}