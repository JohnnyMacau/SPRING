package com.macauslot.recruitment_ms.aop;


import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.macauslot.recruitment_ms.entity.ApplicantInfo;
import com.macauslot.recruitment_ms.exception.DuplicateIdCardNumException;
import com.macauslot.recruitment_ms.exception.DuplicateKeyException;
import com.macauslot.recruitment_ms.exception.DuplicateUserNameException;
import com.macauslot.recruitment_ms.exception.PasswordFormatException;
import com.macauslot.recruitment_ms.exception.PasswordNotMatchException;

@Component
@Aspect
public class LogAspect {


    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);


    @Around(value = "execution(* com.macauslot.recruitment_ms.service.impl.*.*(..))")
    public Object showLog(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();


        Signature signature = point.getSignature();
        String methodName = signature.getName();


        Object result;
        try {
            try {
                //目标方法之前要执行的操作,相当于@before
//                System.err.println("[环绕日志]" + methodName + "开始了!");
                log.info("[环绕日志] {} 开始了!", methodName);
                //调用目标方法
                result = point.proceed(args);


            } finally {


                log1(args, methodName);

            }


            log2(methodName, result);


        } catch (Throwable e) {
            //目标方法抛出异常信息之后的操作，相当于@AfterThrowing
        	String errorMsg = "";
        	if (e instanceof DuplicateKeyException
        			||e instanceof DuplicateIdCardNumException
        			||e instanceof DuplicateUserNameException
        			||e instanceof PasswordNotMatchException
        			||e instanceof PasswordFormatException){
        		errorMsg = e.getMessage();
        	}
        	else{
        		errorMsg = e.toString();
        	}
//            System.err.println("[环绕日志]" + methodName + "出异常了，异常对象为:" + errorMsg);
            log.info("[环绕日志] {} 出异常了，异常对象为:{} ", methodName, errorMsg);
            throw e;
        }
        return result;
    }


    //目标方法正常执行之后的操作，相当于@AfterReturning
    private static void log2(String methodName, Object result) {
        if (result instanceof ApplicantInfo) {
            ApplicantInfo applicantInfo = new ApplicantInfo();
            applicantInfo.copy((ApplicantInfo) result);
            applicantInfo.setIdCardNumber("***");
            applicantInfo.setPassword("***");
//            System.err.println("[环绕日志]" + methodName + "返回了，返回值为HH:" + applicantInfo);
            log.info("[环绕日志] {} 返回了，返回值为: {} ", methodName, applicantInfo);
        } else {
            if ("getToken".equals(methodName)) {
//                System.err.println("[环绕日志]" + methodName + "返回了，返回值为: ***");
                log.info("[环绕日志] {} 返回了，返回值为: *** ", methodName);
            } else {
//                System.err.println("[环绕日志]" + methodName + "返回了，返回值为:" + result);
                log.info("[环绕日志] {} 返回了，返回值为: {} ", methodName, result);
            }
        }
    }


    //方法最终结束时执行的操作，相当于@after
    private static void log1(Object[] args, String methodName) {
        if ("login".equals(methodName)) {
            args[1] = "***";
        } else if ("changePassword".equals(methodName)) {
            args[1] = "***";
            args[2] = "***";
        } else if ("updateTop1".equals(methodName)) {
            if (args[1] instanceof ApplicantInfo) {
                args[1] = copyToApplicantInfo(args[1]);
            }
        } else if ("updateTop4".equals(methodName)) {
            if (args[2] instanceof ApplicantInfo) {
                args[2] = copyToApplicantInfo(args[2]);
            }
        } else if ("getToken".equals(methodName)) {
            args[0] = "***";
        } else if ("sendForgetPwdMail".equals(methodName)) {
            if (args[0] instanceof ApplicantInfo) {
                args[0] = copyToApplicantInfo(args[0]);
            }
        } else if ("sendTemplateMailByAsynchronousMode".equals(methodName)) {
            if (args[0] instanceof ApplicantInfo) {
                args[0] = copyToApplicantInfo(args[0]);
            }
        }
        List<Object> asList = Arrays.asList(args);
//        System.err.println("[环绕日志]" + methodName + "结束了，参数为:" + asList);
        log.info("[环绕日志] {} 结束了，参数为: {}  ", methodName, asList);
    }

    private static Object copyToApplicantInfo(Object arg) {
        ApplicantInfo applicantInfo = new ApplicantInfo();
        applicantInfo.copy((ApplicantInfo) arg);
        applicantInfo.setIdCardNumber("***");
        applicantInfo.setPassword("***");
        return applicantInfo;
    }
}





