package com.macauslot.recruitment_ms.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XSSEscapeFilter implements Filter {


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;


        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");

        boolean allowedPath = AddHeader.ALLOWED_PATHS.contains(path);
        if (allowedPath) {
//这里是不需要处理的url进入的方法
// System.err.println(path);
            chain.doFilter(req, resp);
        } else {
            //后面会有 XssHttpServletRequestWrapper 的代码。这个类是自己定义的
//            System.err.println("-------处于XSS过滤中---------");
            chain.doFilter(new XssHttpServletRequestWrapper(request), response);
        }


    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }


}
