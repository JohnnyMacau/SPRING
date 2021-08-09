package com.macauslot.recruitment_ms.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@WebFilter(filterName = "AddHeader", urlPatterns = {"/*"})
@Order(1)

public class AddHeader implements Filter {


    static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/index.html", "/contact.html")));


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);

        if (allowedPath) {
//            System.out.println("这里是不需要处理的url进入的方法");
            chain.doFilter(req, resp);
        } else {
//            System.out.println("这里是需要处理的url进入的方法");
//        chrome和Safari 是不支持x-frame-options的?   [; frame-ancestors 'self' 'https://www.goalslot.cn/'  ]
//        需要加入
            response.setHeader("Content-Security-Policy", "default-src 'self'; style-src 'self' 'unsafe-inline'  ");
            response.setHeader("X-Content-Security-Policy", "default-src 'self'; style-src 'self' 'unsafe-inline'  ");
            response.setHeader("X-WebKit-CSP", "default-src 'self'; style-src 'self' 'unsafe-inline'  ");
            //实际设置
            response.setHeader("x-frame-options", "SAMEORIGIN");
            /*禁用缓存*/
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragrma", "no-cache");
            response.setDateHeader("Expires", 0);




            chain.doFilter(req, resp);
        }


    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
