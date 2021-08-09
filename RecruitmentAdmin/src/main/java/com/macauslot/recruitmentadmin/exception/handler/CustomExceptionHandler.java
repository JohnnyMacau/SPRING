package com.macauslot.recruitmentadmin.exception.handler;


import com.macauslot.recruitmentadmin.exception.*;
import com.macauslot.recruitmentadmin.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {


    private static final Map<Class<? extends Exception>, Integer> STATECODE_FOR_VISITOR_MAP;
    private static final Map<Class<? extends Exception>, Integer> STATECODE_FOR_ITD_MAP;


    static {
        Map<Class<? extends Exception>, Integer> tmpMap = new HashMap<>();

        // 301-用戶非法繞過驗證異常
        tmpMap.put(UserSkipValidation.class, 301);

        tmpMap.put(UserDataMissingException.class, 302);


        tmpMap.put(UserOrPasswordNotMatchException.class, 400);
        tmpMap.put(DataNotFoundException.class, 404);


        tmpMap.put(DuplicateKeyException.class, 408);


        //(校验异常-@RequestBody类型)
        tmpMap.put(MethodArgumentNotValidException.class, 409);
        //(校验异常-@PathVariable和@RequestParam类型)
        tmpMap.put(ConstraintViolationException.class, 410);

        // 602-数字转换异常
        tmpMap.put(NumberFormatException.class, 602);
        // 603-日期转换异常
        tmpMap.put(ParseException.class, 603);

        // 604-邮件发送异常
        tmpMap.put(SendEmailException.class, 604);
        // 609-token不存在
        tmpMap.put(TokenNotFoundException.class, 609);
        // 610-token不匹配
        tmpMap.put(TokenNotMatchException.class, 610);


        STATECODE_FOR_VISITOR_MAP = Collections.unmodifiableMap(tmpMap);

        Map<Class<? extends Exception>, Integer> tmpMap2 = new HashMap<>();

        // 700-EntityUtils异常
        tmpMap2.put(EntityCastException.class, 700);
        tmpMap2.put(IllegalArgumentException.class, 701);
        tmpMap2.put(FormRepeatException.class, 622);

        STATECODE_FOR_ITD_MAP = Collections.unmodifiableMap(tmpMap2);

    }


    @ExceptionHandler(UserLoginTimeoutException.class)
    public ModelAndView handleUserLoginTimeoutException(HttpServletRequest req, Exception e, Model model) {
        String msg = e.getMessage();
        System.err.println("<<登錄超時>>");
        log.info("<<登錄超時>>--- Host地址: {} invokes url: {} INFO: {}", req.getRemoteHost(), req.getRequestURL(), msg);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", msg);
        modelAndView.setViewName("/backend/login");
        return modelAndView;
    }


    @ExceptionHandler(Exception.class)
    public Object handlerException(HttpServletRequest request, Exception e) {
        Integer stateCode = STATECODE_FOR_VISITOR_MAP.get(e.getClass());


        if (stateCode != null) {

            String msg = e.getMessage();
            System.err.println(stateCode + " --> " + msg);
            log.info("<<捕获异常>>--- {} --> Host地址: {} invokes url: {} INFO: {}", stateCode, request.getRemoteHost(), request.getRequestURL(), msg);


            ResponseResult<Void> responseResult = new ResponseResult<>(500, e);

            if (stateCode == 603) {//e instanceof ParseException
                responseResult.setMessage("日期转换异常");
            } else if (stateCode == 602) {//e instanceof NumberFormatException
                responseResult.setMessage("格式不正確,應為數字格式");
            } else if (e instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
                List<String> errorInformation = ex.getBindingResult().getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage).collect(Collectors.toList());
                responseResult.setMessage("backend validation: " + errorInformation.toString());
            } else if (e instanceof ConstraintViolationException) {
                ConstraintViolationException ex = (ConstraintViolationException) e;
                List<String> errorInformation = ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                responseResult.setMessage("backend validation: " + errorInformation.toString());
            }
            String viewName = "/error/error";
            return returnResult(request, e, responseResult, viewName);

        } else {
            Integer stateCodeItd = STATECODE_FOR_ITD_MAP.get(e.getClass());

            e.printStackTrace();


            String exception = "";

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 PrintStream s = new PrintStream(baos)) {
                e.printStackTrace(s);
                exception = baos.toString();
            } catch (Exception ex) {
                log.error("<<CustomExceptionHandler处理ByteArrayOutputStream出错>>--- {} --> Host地址: {} invokes url: {} ERROR: {}", 9999, request.getRemoteHost(), request.getRequestURL(), ex.getMessage());
            }


            if (stateCodeItd == null) {
                log.error("<<捕获未知异常>>--- {} --> Host地址: {} invokes url: {} ERROR: {}", 999, request.getRemoteHost(), request.getRequestURL(), StringUtils.isBlank(exception) ? e.getMessage() : exception);
            } else if (stateCodeItd == 622) {
                log.info("<<捕获ITD异常(用戶不顯示,內部查看)>>--- {} --> Host地址: {} invokes url: {} ERROR: {}", stateCodeItd, request.getRemoteHost(), request.getRequestURL(), StringUtils.isBlank(exception) ? e.getMessage() : exception);
            } else {
                log.error("<<捕获ITD异常(用戶不顯示,內部查看)>>--- {} --> Host地址: {} invokes url: {} ERROR: {}", stateCodeItd, request.getRemoteHost(), request.getRequestURL(), StringUtils.isBlank(exception) ? e.getMessage() : exception);
            }


            ResponseResult<Void> responseResult = new ResponseResult<>(999, "系統內部錯誤,請聯繫管理員!");

            String viewName = "/error/error_5xx";
            return returnResult(request, e, responseResult, viewName);

        }

    }

    private Object returnResult(HttpServletRequest request, Exception e, ResponseResult<Void> responseResult, String viewName) {
        //检查请求是否为ajax, 如果是 ajax 请求则返回 Result json串, 如果不是 ajax 请求则返回 error 视图
        String contentTypeHeader = request.getHeader("Content-Type");
        String acceptHeader = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        if ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            return responseResult;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", responseResult.getMessage());
            modelAndView.addObject("url", request.getRequestURL());
            modelAndView.addObject("ex", e);
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.setViewName(viewName);
            return modelAndView;
        }
    }


}
