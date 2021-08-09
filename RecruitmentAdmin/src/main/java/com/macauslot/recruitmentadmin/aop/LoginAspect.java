package com.macauslot.recruitmentadmin.aop;

import com.macauslot.recruitmentadmin.annotation.UserLoginvalidation;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.exception.UserLoginTimeoutException;
import com.macauslot.recruitmentadmin.exception.UserSkipValidation;
import com.macauslot.recruitmentadmin.util.ServerRoleTagEnum;
import com.macauslot.recruitmentadmin.util.SessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Order(3)

public class LoginAspect {


    @Around(value = "execution(* com.macauslot.recruitmentadmin.controller.*.*(..)) && @annotation(userLoginvalidation)")
    public Object checkLogin(ProceedingJoinPoint point, UserLoginvalidation userLoginvalidation) throws Throwable {
        Object[] args = point.getArgs();


        Signature signature = point.getSignature();
        String methodName = signature.getName();

        Object result;
//        try {
//            try {
        //目标方法之前要执行的操作,相当于@before
        System.err.println("[@userLoginvalidation]: " + methodName);


        UserDTO userDTO = SessionUtil.getUserFromSession("user");
        if (userDTO != null) {
            if (userLoginvalidation.needSetUserDTO()) {
                for (int i = 0, len = args.length; i < len; i++) {
                    if (args[i] instanceof UserDTO) {
                        args[i] = userDTO;
                        break;
                    }
                }
            }

            ServerRoleTagEnum currentRoleTag = userDTO.getRoleTag();
            if (Arrays.stream(userLoginvalidation.serverRoleTagEnum()).noneMatch(x -> x == currentRoleTag)) {
                throw new UserSkipValidation("warning: 你沒有該權限[HR]");
            }


        } else {
            throw new UserLoginTimeoutException("用戶已過期,請重新登錄");
        }


        //调用目标方法
        result = point.proceed(args);


//            } finally {


//                log1(args, methodName);

//            }


//            log2(methodName, result);


//        } catch (Throwable e) {
//            //目标方法抛出异常信息之后的操作，相当于@AfterThrowing
//
//            throw e;
//        }
        return result;
    }


//    //方法最终结束时执行的操作，相当于@after
//    private static void log1(Object[] args, String methodName) {
//    }


}





