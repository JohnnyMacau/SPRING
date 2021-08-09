package com.macauslot.recruitment_ms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
class MainsiteErrorController implements ErrorController {
    private static final Logger log = LoggerFactory.getLogger(MainsiteErrorController.class);

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) {
//        获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        log.error("---handleError()其它非controller层错误---HttpStatusCode: {}  Host地址: {} invokes url: {} ", statusCode, request.getRemoteHost(), request.getRequestURL());
//        System.err.println(request.getRequestURL());
//        System.err.println(request.getRequestURI());
//        System.err.println(request.getContextPath());
//        System.err.println(request.getServletPath());

        try {
            System.err.println("|error|,返回主页");
//            response.sendRedirect("/index.html");
            RequestDispatcher view = request.getRequestDispatcher("/hr_top_m1.html");//  /index.html
            view.forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
