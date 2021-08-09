package com.macauslot.recruitment_ms.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.macauslot.recruitment_ms.util.SessionUtil;


/**
 * 拦截器
 *
 * @author Administrator
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //拦截规则
        //如果未登录，重定向到登录，并拦截
        //如果已经登录，直接放行
//        String target = request.getServletPath();


        if (SessionUtil.getVisitorIdFromSession() == null) {
            System.err.println("拦截未登录");
            log.info("---拦截未登录--- Host地址: {} invokes url: {} ", request.getRemoteHost(), request.getRequestURL());
            try {
//                您还没登入,请先登入再来此版面!
//                response.sendRedirect("/noLogin.html");
                RequestDispatcher view = request.getRequestDispatcher("/noLogin.html");
                view.forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(" request.getRequestDispatcher(\"/noLogin.html\");");
            }
            return false;
        }
       /* else if (target.contains("hr_top")) {
            int targetPage = Integer.parseInt(String.valueOf(target.charAt(7)));
            int incompletePageNo = Integer.parseInt(String.valueOf(SessionUtil.getSessionAttribute("incompletePageNo")));

            if (targetPage > incompletePageNo) {
                System.err.println("targetPage:" + targetPage);
                System.err.println("incompletePageNo:" + incompletePageNo);


                try {
                    //还有没完成的页面,请填写完再来!
//                    response.sendRedirect("/notFinish.html?" + incompletePageNo);
                    request.getRequestDispatcher("/notFinish.html?" + incompletePageNo).forward(request, response);
                } catch (ServletException | IOException e) {
                    e.printStackTrace();
                    log.error("  request.getRequestDispatcher(\"/notFinish.html?\" + incompletePageNo);");
                }
                return false;
            } else {
                return true;
            }
        } */

        else {
            return true;
        }


    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}

