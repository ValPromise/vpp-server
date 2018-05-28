package com.vpp.common.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.core.annotation.Order;

@Order(1)
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
public class SessionFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(SessionFilter.class);

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

//        logger.info("url:::{}", uri);
        Map<String, String> requestMap = getMapByRequest(request);
//        logger.info("params:::{}", requestMap);
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