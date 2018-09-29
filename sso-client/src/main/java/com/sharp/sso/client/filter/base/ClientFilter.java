package com.sharp.sso.client.filter.base;

import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class ClientFilter extends ParamFilter implements Filter {

    //匹配请求路径（？匹配一个字符，*匹配0个或多个字符，**匹配0个或多个目录）
    protected String pattern;

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * 定义抽象方法，是否允许访问，允许访问返回true
     */
    public abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 判断是否为ajax请求
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    protected void responseJson(HttpServletResponse response, String resultCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = response.getWriter();
        writer.write("{\"resultCode\":" + resultCode + ",\"message\":\"" + message + "\"}");
        writer.flush();
        writer.close();
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
