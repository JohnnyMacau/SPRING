package com.macauslot.recruitment_ms.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.macauslot.recruitment_ms.annotation.PassToken;
import com.macauslot.recruitment_ms.annotation.XsrfToken;
import com.macauslot.recruitment_ms.exception.TokenNotFoundException;
import com.macauslot.recruitment_ms.exception.TokenNotMatchException;
import com.macauslot.recruitment_ms.util.EncryptUtil;
import com.macauslot.recruitment_ms.util.SessionUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;


/**
 *
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

//    @Autowired
//    IApplicantInfoService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object)   {

        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(XsrfToken.class)) {
            XsrfToken xsrfToken = method.getAnnotation(XsrfToken.class);
            if (xsrfToken.required()) {
                // 执行认证
                /**
                 * 使用定制的HTTP报头：
                 *
                 * 如果执行交易的所有请求都使用XMLHttpRequest并附加一个定制的HTTP报头，
                 * 同时拒绝缺少定制报头的任何请求，就可以用XMLHttpRequest API来防御CSRF攻击。
                 * 由于浏览器通常仅准许站点将定制的HTTP报头发送给相同站点，从而了防止由CSRF攻击的源站点所发起的交易。
                 */
                String token = httpServletRequest.getHeader("X-XSRF-TOKEN");// 从 http 请求头中取出 token
                if (token == null) {
                    throw new TokenNotFoundException("无token，请重新登录");
                }

                 // 验证 token
                HttpSession session = SessionUtil.getSession();
                if (session == null) {
                    System.err.println("拦截---session is null");
                    return false;
                }
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(EncryptUtil.getMd5Token( session.getId()))).build();

                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new TokenNotMatchException("token验证出错JWTVerificationException");
                }
                return true;
            }
        }
        //不加注解的话默认不验证.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
