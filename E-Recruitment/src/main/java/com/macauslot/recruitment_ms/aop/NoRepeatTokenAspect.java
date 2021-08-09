package com.macauslot.recruitment_ms.aop;

import com.macauslot.recruitment_ms.annotation.NoRepeatToken;
import com.macauslot.recruitment_ms.exception.FormRepeatException;
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
 * 防重復提交
 */
@Aspect
@Component
public class NoRepeatTokenAspect {

    private static final Logger logger = LoggerFactory.getLogger(NoRepeatTokenAspect.class);

    @Before("(within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)) && @annotation(noRepeatToken)")
    public void testToken(final JoinPoint joinPoint, NoRepeatToken noRepeatToken) {
        try {
            if (noRepeatToken != null) {
                //獲取 joinPoint 的全部參數
                Object[] args = joinPoint.getArgs();
                HttpServletRequest request = null;
//                HttpServletResponse response = null;


                String methodName;

                String cs = noRepeatToken.consumeMethodName();
                if (StringUtils.isBlank(cs)) {
                    methodName = joinPoint.getSignature().getName();
                } else {
                    methodName = cs;
                }


                for (Object arg : args) {
                    //獲得參數中的 request && response
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

                List<String> serverTokenList = (List<String>) request.getSession()  //getSession()==getSession(true),沒有會話會生成
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
                    logger.debug("進入表單頁面，Token值為：" + serverTokenList);
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
            logger.error("noRepeatToken 發生異常 : " + e);
        }
    }

    private void checkRepeatSubmit(HttpServletRequest request, List<String> serverTokenList) throws FormRepeatException {

        if (serverTokenList == null) {
            logger.error("session 為空");
            throw new FormRepeatException("session 為空");
        }


        String clientToken = request.getParameter("noRepeatToken");

        if (StringUtils.isBlank(clientToken)) {
            logger.error("請從正常頁面進入！");
            throw new FormRepeatException("請從正常頁面進入！");
        }

        System.err.println(clientToken);
        if (serverTokenList.isEmpty() || !serverTokenList.contains(clientToken)) {
            logger.error("表單重復提交");
            throw new FormRepeatException("表單重復提交");
        }

        serverTokenList.remove(clientToken);
        logger.debug("沒有重復提交：表單頁面Token值為：" + clientToken + ",Session中余下的Token值為:" + serverTokenList);

    }


}
