package com.demo.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.entity.Users;
import com.demo.repository.UserRepository;
import com.demo.util.PageInfo;
import com.demo.util.ResponseResult;
import com.demo.vo.UserVO;


@Controller
@RequestMapping("/user")
public class UserController {

	public final Integer success = 200;
	public final Integer fail = 500;
//    private final IUserService userService;

	@Autowired
	private  UserRepository userRepository;
	
//    @Value("${hr.supervisors}")
//    private String[] hrSupervisors;
    
    @Resource
    private Environment env;


//    @Autowired
//    public DemoController(IUserService userService) {
//        this.userService = userService;
//    }
    
    @RequestMapping(value = "/main")
    public String main(HttpServletRequest request) {
        return "/main";
    }
    
    @RequestMapping(value = "/goLogin")
    public String goLogin(HttpServletRequest request) {
        return "/login";
    }
    
    @RequestMapping(value = "/login")
    @ResponseBody
    public ResponseResult login(HttpServletRequest request) {
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	
    	List<Users> userList = userRepository.getUsers(username, password);
    	if(userList.size()>0) {
    		request.getSession().setAttribute("user", userList.get(0));
    		return new ResponseResult<>(success);
    	}
    	else {
    		return new ResponseResult<>(fail);
    	}
    }

    @GetMapping(value = "/delete/session")
  public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {

      session.invalidate();

      return "/login";
  }

    
    @RequestMapping(value = "/page1")
    public String page1(HttpServletRequest request) {
        return "/page1";
    }
    
    @RequestMapping(value = "/page2")
    public String page2(HttpServletRequest request) {
        return "/page2";
    }
    

    @RequestMapping("/searchUser")
    @ResponseBody
    public PageInfo<Users> searchApplication(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            UserVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return PageInfo.convertPage2PageInfo(draw, userRepository.searchUser(start, length, condition, sidx, sord));
 
    }

}