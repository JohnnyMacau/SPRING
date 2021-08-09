package com.macauslot.recruitment_ms.exception.handler;

import com.macauslot.recruitment_ms.controller.exception.RequestException;
import com.macauslot.recruitment_ms.exception.*;
import com.macauslot.recruitment_ms.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * 正确响应时的代号
     */
//    public static final Integer SUCCESS = 200;

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    private static final Map<Class<? extends Exception>, Integer> STATECODE_FOR_VISITOR_MAP ;
    private static final Map<Class<? extends Exception>, Integer> STATECODE_FOR_ITD_MAP  ;


    static {
        Map<Class<? extends Exception>, Integer>  tmpMap= new HashMap<>();
        // 400-违反了Unique约束的异常,注册时候用户名和身份证都重复
        tmpMap.put(DuplicateKeyException.class, 400);
        // 401-违反了Unique约束的异常,注册时候身份证重复
        tmpMap.put(DuplicateIdCardNumException.class, 401);
        // 402-违反了Unique约束的异常,注册时候用户名重复
        tmpMap.put(DuplicateUserNameException.class, 402);
        // 403-用户数据不存在
        tmpMap.put(UserNotFoundException.class, 403);
        // 404-密码错误
        tmpMap.put(PasswordNotMatchException.class, 404);
        // 405-数据不存在
        tmpMap.put(DataNotFoundException.class, 405);
        // 406-访问被拒绝的异常
        tmpMap.put(AccessDeniedException.class, 406);
        // 301-用户名格式错误
        tmpMap.put(UsernameFormatException.class, 301);
        // 302-密码格式错误
        tmpMap.put(PasswordFormatException.class, 302);
        // 500-插入数据异常
        tmpMap.put(InsertException.class, 500);
        // 501-更新数据异常
        tmpMap.put(UpdateException.class, 501);
        // 600-请求异常
        tmpMap.put(RequestException.class, 600);
        // 603-日期转换异常
        tmpMap.put(ParseException.class, 603);
        // 604-邮件发送异常
        tmpMap.put(EmailException.class, 604);
        // 609-token不存在
        tmpMap.put(TokenNotFoundException.class, 609);
        // 610-token不匹配
        tmpMap.put(TokenNotMatchException.class, 610);
        tmpMap.put(SessionTimeOutAfterLoginException.class, 611);
        tmpMap.put(SessionTimeOutInRegisterException.class, 612);
        tmpMap.put(SessionTimeOutException.class, 620);
        tmpMap.put(FormRepeatException.class, 622);
        tmpMap.put(RejectOldDataException.class, 630);

        STATECODE_FOR_VISITOR_MAP=Collections.unmodifiableMap(tmpMap);

        Map<Class<? extends Exception>, Integer>  tmpMap2= new HashMap<>();
        // 502-删除数据异常
        tmpMap2.put(DeleteException.class, 502);
        // 700-EntityUtils异常
        tmpMap2.put(EntityCastException.class, 700);
        STATECODE_FOR_ITD_MAP=Collections.unmodifiableMap(tmpMap2);

    }


    @ExceptionHandler({
            ServiceException.class,
            RequestException.class,
            ParseException.class
    })
    public ResponseResult<Void> handleException(HttpServletRequest req, Exception e) {

        Integer state = STATECODE_FOR_VISITOR_MAP.get(e.getClass());



        System.err.println(state + " --> " + e.getMessage());
        log.error("---handleException()捕获异常--- {} --> Host地址: {} invokes url: {} ERROR: ", state, req.getRemoteHost(), req.getRequestURL(), e.getMessage());
        return new ResponseResult<>(state, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> defaultErrorHandler(HttpServletRequest req, Exception e) {

        Integer state = STATECODE_FOR_ITD_MAP.get(e.getClass());


        if (state!=null){
            log.error("---defaultErrorHandler()捕获异常--- {} --> Host地址: {} invokes url: {} ERROR: {}", state, req.getRemoteHost(), req.getRequestURL(), e.getMessage(), e);
        }else {
            state = 999;
            e.printStackTrace();
            log.error("---defaultErrorHandler()其它异常--- {} --> Host地址: {} invokes url: {} ERROR: {}", state, req.getRemoteHost(), req.getRequestURL(), e.getMessage());
        }



        return new ResponseResult<>(999);
    }


}
