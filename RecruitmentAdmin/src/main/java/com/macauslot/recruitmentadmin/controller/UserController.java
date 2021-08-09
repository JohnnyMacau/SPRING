package com.macauslot.recruitmentadmin.controller;


import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.dto.UserPO;
import com.macauslot.recruitmentadmin.exception.UserSkipValidation;
import com.macauslot.recruitmentadmin.service.IUserService;
import com.macauslot.recruitmentadmin.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Integer SUCCESS = 200;
    public static final String USER_DEPT_HR = "HR";

    private final IUserService userService;

    @Value("${hr.supervisors}")
    private String[] hrSupervisors;
    
    @Value("${flt.admins}")
    private String[] fltAdmins;

   /* @Resource
    private Environment env;*/


    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;

    }

    @PostMapping(value = "/login")
    @ResponseBody

    public ResponseResult<Void> handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session
    ) {
//        System.err.println("PWD:" + password);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new UserSkipValidation("用戶或密碼不能為空");
        }
        UserPO userpo = userService.login(username, password);
        UserDTO userDTO = new UserDTO();
        BeanCopyUtil.beanCopyExceptNull(userpo, userDTO);
        setRoleTag(userDTO);
        String employeeId = userDTO.getEmployeeId();
        userDTO.setIsHrSupervisor(Arrays.asList(hrSupervisors).contains(employeeId));
        userDTO.setIsFLTAdmin(Arrays.asList(fltAdmins).contains(employeeId));

        setAttribute(session, userDTO, "user");
        System.err.println(userDTO);
        return new ResponseResult<>(SUCCESS);
    }




    private void setRoleTag(UserDTO userDTO) {
        if (USER_DEPT_HR.equals(userDTO.getDeptCode())) {
            userDTO.setRoleTag(ServerRoleTagEnum.HR);
            return;
        }

        userDTO.setRoleTag(ServerRoleTagEnum.DEPT);
    }

    private void setAttribute(HttpSession session, UserDTO userDTO, String user) {

        System.err.println("id1-->" + session.getId());
        session = SessionUtil.reGenerateSessionId();
        System.err.println("id2-->" + session.getId());
        SessionUtil.setSessionAttribute(user, userDTO);
        System.err.println("用户登录,更换sessionID <---->");


    }


    @GetMapping(value = "/delete/session")
//    @ResponseBody
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
//        Integer visitorId = SessionUtil.getVisitorIdFromSession();
//        System.err.println("visitorId-----" + visitorId);
        session.invalidate();
        System.err.println("用戶logout");
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                String name = cookie.getName();
//                if ("FLT-ID".equals(name) ||
//                        "status".equals(name) ||
//                        "XSRF-TOKEN".equals(name)
//                ) {
//                    cookie.setMaxAge(0);
//                    cookie.setPath("/");
//                    response.addCookie(cookie);
////                    System.err.println("删除会话cookie");
//                }
//
//
//            }
//        }
        return "/backend/login";
    }


}
