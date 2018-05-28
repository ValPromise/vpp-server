/*
 * 文  件  名：BlackUtil.java
 * 版         权：Copyright 2016 Lxl Rights Reserved.
 * 描         述：
 * 修  改  人：Lxl
 * 修改时间：2018年5月21日
 * 修改内容：新增
 */
package com.vpp.common.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

/**
 * TODO 添加类的描述
 * 
 * @author Lxl
 * @version V1.0 2018年5月21日
 */
public class BlackUtil {

    public static void printError(HttpServletResponse response) {
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
        } catch (IOException e) {
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
}
