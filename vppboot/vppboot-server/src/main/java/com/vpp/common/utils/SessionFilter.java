package com.vpp.common.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.gson.Gson;
import com.vpp.common.vo.ResultVo;

@Order(1)
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
public class SessionFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(SessionFilter.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    LoginUtils loginUtils;

    @Value("${origin.url}")
    String originUrl;

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

        // 不拦截url集合
        List<String> passList = new ArrayList<String>();
        passList.add("/login/login");
        passList.add("/login/logout");
        passList.add("/login/getMenu");

        String method = request.getMethod();
        if (!ConstantsServer.OPTIONS.equals(method)) {
            if (!passList.contains(uri)) {
//                Long account = loginUtils.getLoginAccount(request);
//                if (null == account) {
//                    this.printError(response);
//                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void printError(HttpServletResponse response) {
        // ------------------out.print 参数设置 start--------
        // 设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        response.addHeader("Access-Control-Allow-Origin", originUrl);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        // 允许的访问方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "token,Origin, X-Requested-With, Content-Type, Accept");
        response.setContentType("application/json;charset=UTF-8");
        // ------------------out.print 参数设置 end--------

        Gson gson = new Gson();
        String err = gson.toJson(
                ResultVo.setResultError("...---=== U-n-a-u-t-h-o-r-i-z-e-d ===---...", ConstantsServer.ErrorCode.AUTH_FAIL));

        PrintWriter writer = null;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            writer = new PrintWriter(osw, true);
            writer.write(err);
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