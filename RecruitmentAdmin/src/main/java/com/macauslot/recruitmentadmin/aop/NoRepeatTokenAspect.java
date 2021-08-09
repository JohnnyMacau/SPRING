package com.macauslot.recruitmentadmin.aop;


import com.macauslot.recruitmentadmin.annotation.NoRepeatToken;
import com.macauslot.recruitmentadmin.exception.FormRepeatException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 防重复提交
 */
@Aspect
@Component
public class NoRepeatTokenAspect {

    private static final Logger logger = LoggerFactory.getLogger(NoRepeatTokenAspect.class);

    @Before("(within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)) && @annotation(noRepeatToken)")
    public void testToken(final JoinPoint joinPoint, NoRepeatToken noRepeatToken) {
        try {
            if (noRepeatToken != null) {
                //获取 joinPoint 的全部参数
                Object[] args = joinPoint.getArgs();
                HttpServletRequest request = null;
//                HttpServletResponse response = null;


                String methodName;


                String p = noRepeatToken.produceMethodName();
                String cs = noRepeatToken.consumeMethodName();

                if (noRepeatToken.producer() && StringUtils.isNotBlank(p)) {
                    methodName = p;
                } else if (StringUtils.isBlank(cs)) {
                    methodName = joinPoint.getSignature().getName();
                } else {
                    methodName = cs;
                }


                for (Object arg : args) {
                    //获得参数中的 request && response
                    if (arg instanceof HttpServletRequest) {
                        request = (HttpServletRequest) arg;
                    }
                   /* if (arg instanceof HttpServletResponse) {
                        response = (HttpServletResponse) arg;
                    }*/
                }
                if (request == null) {
                    throw new Exception("request is null");
                }

                List<String> serverTokenList = (List<String>) request.getSession()  //getSession()==getSession(true),没有会话会生成
                        .getAttribute(methodName + "NoRepeatTokenList");


                if (noRepeatToken.producer()) {
                    if (serverTokenList == null) {
                        serverTokenList = new ArrayList<>();
                    }
                    int oldListSize = serverTokenList.size();
                    int needMoreTokenNum = noRepeatToken.needProduceTokenNum() - oldListSize;
                    for (int i = 0; i < needMoreTokenNum; i++) {
                        serverTokenList.add(UUID.randomUUID().toString());
                    }
                    logger.debug("进入表单页面，Token值为：" + serverTokenList);
                }

                if (noRepeatToken.consumer()) {
                    checkRepeatSubmit(request, serverTokenList);
//                    request.getSession(false).removeAttribute("noRepeatToken");
                }


                request.getSession().setAttribute(methodName + "NoRepeatTokenList", serverTokenList);


            }

        } catch (FormRepeatException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("noRepeatToken 发生异常 : " + e);
        }
    }

    private void checkRepeatSubmit(HttpServletRequest request, List<String> serverTokenList) throws FormRepeatException {

        if (serverTokenList == null) {
            logger.error("session 为空");
            throw new FormRepeatException("session 为空");
        }


        String clientToken = request.getParameter("noRepeatToken");

        if (StringUtils.isBlank(clientToken)) {
            logger.error("请从正常页面进入！");
            throw new FormRepeatException("请从正常页面进入！");
        }

        System.err.println(clientToken);
        if (serverTokenList.isEmpty() || !serverTokenList.contains(clientToken)) {
            logger.error("表单重复提交");
            throw new FormRepeatException("表单重复提交");
        }

        serverTokenList.remove(clientToken);
        logger.debug("没有重复提交：表单页面Token值为：" + clientToken + ",Session中余下的Token值为:" + serverTokenList);

    }


}
