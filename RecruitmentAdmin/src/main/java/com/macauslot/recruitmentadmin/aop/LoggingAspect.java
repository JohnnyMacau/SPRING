package com.macauslot.recruitmentadmin.aop;



import com.macauslot.recruitmentadmin.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
@Slf4j
public class LoggingAspect {



    String strLog = null ;

    @Before(value = "execution(* com.macauslot.recruitmentadmin.repository.*.*(..))")
    public void daoLogBefore(JoinPoint point) throws Throwable {
    	 String strLog = "Begining method: "
         		+ point.getTarget().getClass().getName() + "."
         		+ point.getSignature().getName();
         log.info(strLog);
         String[] parms = ((MethodSignature) point.getSignature()).getParameterNames();
         if(parms==null)return;
         Object[] args = point.getArgs();
         for(int i=0;i<parms.length;i++){
         	if(parms[i].contains("password")||parms[i].contains("Password")||parms[i].contains("pwd")||parms[i].contains("Pwd")){
         		log.info(parms[i] + ":******");
         	}
         	else{
         	log.info(parms[i] + ":" + (args[i]==null?"null":args[i].toString()));
         	}
         }
    }

    @Around(value = "execution(* com.macauslot.recruitmentadmin.repository.*.*(..))")
    public Object daoLogAround(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        time = System.currentTimeMillis() - time;
        strLog = "Process time for "+ pjp.getSignature().getName() + ": " + time + " ms";
        log.info(strLog);
        return retVal;
    }


    @After(value = "execution(* com.macauslot.recruitmentadmin.repository.*.*(..))")
    public void daoLogAfter(JoinPoint point) throws Throwable {
        strLog ="Ending method: "
                + point.getTarget().getClass().getName() + "."
                + point.getSignature().getName();
        log.info(strLog);
    }

    @Before(value = "execution(* com.macauslot.recruitmentadmin.controller.*.*(..))")
    public void controllerDoBefore(JoinPoint jp) {
        strLog = "Begining method: "
                + jp.getTarget().getClass().getName() + "."
                + jp.getSignature().getName();
        log.info(strLog);
    }

    @Around(value = "execution(* com.macauslot.recruitmentadmin.controller.*.*(..))")
    public Object controllerDoAround(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        time = System.currentTimeMillis() - time;
        strLog = "Process time for "+ pjp.getSignature().getName() + ": " + time + " ms";
        log.info(strLog);
        return retVal;
    }

    @After(value = "execution(* com.macauslot.recruitmentadmin.controller.*.*(..))")
    public void controllerDoAfter(JoinPoint jp) {
        strLog ="Ending method: "
                + jp.getTarget().getClass().getName() + "."
                + jp.getSignature().getName();
        log.info(strLog);
    }

    @AfterReturning(value = "execution(* com.macauslot.recruitmentadmin.controller.*.*(..))", returning = "returnValue")
    public void loggingRepositoryMethods(JoinPoint joinPoint, Object returnValue) {
    	if(returnValue instanceof ResponseResult) {
    	 log.info(returnValue.toString());
    	}
    }
}





