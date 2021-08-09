package com.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.demo.annotation.UserLoginvalidation;

@Component
@Aspect
@Order(3)

public class LoginAspect {


    @Around(value = "execution(* com.demo.controller.*.*(..)) && @annotation(userLoginvalidation)")
    public Object checkLogin(ProceedingJoinPoint point, UserLoginvalidation userLoginvalidation) throws Throwable {
        Object[] args = point.getArgs();


        Signature signature = point.getSignature();
        String methodName = signature.getName();

        Object result = null;


        return result;
    }


//    //方法最终结束时执行的操作，相当于@after
//    private static void log1(Object[] args, String methodName) {
//    }


}





