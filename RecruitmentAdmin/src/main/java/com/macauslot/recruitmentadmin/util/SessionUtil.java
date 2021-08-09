package com.macauslot.recruitmentadmin.util;

import com.macauslot.recruitmentadmin.dto.UserDTO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * session工具类
 *
 * @Author zhk
 * @Date 2018-1-16
 **/


public class SessionUtil {
    /**
     * 全局删除id标示
     */
    public static String GLOB_DELETE_ID_VAL = "globDeleteIdVal";

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    /**
     * 获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession(false);//没有session时返回null,而不是创建新的
    }

    /**
     * 获取真实路径
     *
     * @return
     */
    public static String getRealRootPath() {
        return getRequest().getServletContext().getRealPath("/");
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static String getIp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getRemoteAddr();
        }
        return null;
    }

    /**
     * 获取session中的Attribute
     *
     * @param name
     * @return
     */
    public static Object getSessionAttribute(String name) {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getSession().getAttribute(name);
    }

    /**
     * 设置session的Attribute
     *
     * @param name
     * @param value
     */
    public static void setSessionAttribute(String name, Object value) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.getSession().setAttribute(name, value);
        }
    }

    /**
     * 获取request中的Attribute
     *
     * @param name
     * @return
     */
    public static Object getRequestAttribute(String name) {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getAttribute(name);
    }

    /**
     * 设置request的Attribute
     *
     * @param name
     * @param value
     */
    public static void setRequestAttribute(String name, Object value) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.setAttribute(name, value);
        }
    }

    /**
     * 获取上下文path
     *
     * @return
     */
    public static String getContextPath() {
        return getRequest().getContextPath();
    }

    /**
     * 删除session中的Attribute
     *
     * @param name
     */
    public static void removeSessionAttribute(String name) {
        getRequest().getSession().removeAttribute(name);
    }


    public static UserDTO getUserFromSession(String user) {
        Object temp = getSessionAttribute(user);
        return temp == null ? null : (UserDTO) temp;
    }


    /**
     * 重置sessionid，原session中的数据自动转存到新session中
     */
    public static HttpSession reGenerateSessionId() {
        HttpServletRequest request = getRequest();
        HttpSession session = getSession();

        //首先将原session中的数据转移至一临时map中
        Map<String, Object> tempMap = new HashMap<>(10);
        Enumeration<String> sessionNames = session.getAttributeNames();
        while (sessionNames.hasMoreElements()) {
            String sessionName = sessionNames.nextElement();
            tempMap.put(sessionName, session.getAttribute(sessionName));
        }
        //注销原session，为的是重置sessionId
        session.invalidate();//防止sessionid固定漏洞
        //将临时map中的数据转移至新session
        session = request.getSession();
        for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
        return session;
    }


}
