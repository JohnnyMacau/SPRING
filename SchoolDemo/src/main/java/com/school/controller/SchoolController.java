package com.school.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school.entity.Class;
import com.school.entity.Course;
import com.school.entity.Institude;
import com.school.entity.Student;
import com.school.entity.Users;
import com.school.repository.ClassRepository;
import com.school.repository.CourseRepository;
import com.school.repository.InstitudeRepository;
import com.school.repository.StudentRepository;
import com.school.repository.UniversityRepository;
import com.school.repository.UserRepository;
import com.school.service.SchoolService;
import com.school.util.PageInfo;
import com.school.util.ResponseResult;
import com.school.vo.StendentVO;


@Controller
@RequestMapping("/school")
public class SchoolController {

    private static final Integer SUCCESS = 200;
    private static final Integer FAILURE = 500;
    private static final Logger logger = LoggerFactory.getLogger(DataTablesController.class);

//    private final IUserService userService;

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  StudentRepository studentRepository;
	@Autowired
	private  ClassRepository classRepository;
	@Autowired
	private  SchoolService schoolService;
	@Autowired
	private  CourseRepository courseRepository;
	@Autowired
	private  UniversityRepository universityRepository;
	@Autowired
	private  InstitudeRepository institudeRepository;
	
	
	
//    @Value("${hr.supervisors}")
//    private String[] hrSupervisors;
    
    @Resource
    private Environment env;

    
    @GetMapping(value = "/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
      session.invalidate();
      System.err.println("用戶logout");
      return "/login";
    }
    
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
    		return new ResponseResult<>(SUCCESS);
    	}
    	else {
    		return new ResponseResult<>(FAILURE);
    	}
    }

    
    @RequestMapping(value = "/course")
    public String course(HttpServletRequest request, Model model) {
    	List universityList = universityRepository.findAll();
    	List institudeList = institudeRepository.findAll();
    	model.addAttribute("universityList",universityList);
    	model.addAttribute("institudeList",institudeList);
        return "/course";
    }
    
    @RequestMapping(value = "/student")
    public String student() {
    	return "/student";
    }
    
    @RequestMapping(value = "/student/{id}")
    public String goEditStudent(@PathVariable String id, Model model) {
    	List<Class> classList = classRepository.findAll();
    	Student student = studentRepository.getOne(Integer.parseInt(id));
    	model.addAttribute("classList", classList);
    	model.addAttribute("student",student);
    	return "/add_student";
    }
    @RequestMapping(value = "/delStudent/{id}")
    @ResponseBody
    public ResponseResult<Void> delStudent(@PathVariable String id, Model model) {
    	studentRepository.deleteById(Integer.parseInt(id));
    	return new ResponseResult<>(SUCCESS);
    }
    
    
    
    @RequestMapping(value = "/goAddStudent")
    public String goAddStudent(Model model) {
    	List<Class> classList = classRepository.findAll();
    	
    	Student student = new Student();
    	model.addAttribute("student", student);
    	model.addAttribute("classList", classList);
    	return "/add_student";
    }
    
    @RequestMapping(value = "/saveStudent")
    @ResponseBody
    public ResponseResult<Void> saveStudent(@RequestBody Student student) {
    	studentRepository.save(student);
    	return new ResponseResult<>(SUCCESS);
    }
    
    @PostMapping("/batchDeleteStudent")
    @ResponseBody
    public ResponseResult<Void> batchDeleteStudent(HttpServletRequest request,
                                                     @RequestParam("batchProcess_checkboxs[]") Integer[] ids) {
    	schoolService.batchDeleteStudent(ids);
        return new ResponseResult<>(SUCCESS);
    }

    @RequestMapping("/searchStudent")
    @ResponseBody
    public PageInfo<StendentVO> searchStudent(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            StendentVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return PageInfo.convertPage2PageInfo(draw, studentRepository.searchStudent(start, length, condition, sidx, sord));
 
    }
    
    @PostMapping("/institudeList")
    @ResponseBody
    public ResponseResult<List<Institude>> institudeList(HttpServletRequest request) {
    	String universityId = request.getParameter("universityId");
    	int id = -1;
    	try {
			id = Integer.parseInt(universityId);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<Institude> institudeList = institudeRepository.findByUniversityId(id);
        return new ResponseResult<>(SUCCESS, institudeList);
    }
    
    @RequestMapping("/searchCourse")
    @ResponseBody
    public PageInfo<Course> searchCourse(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length            
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        int id = -1;
        String institudeId = request.getParameter("institudeId");
        try {
			id = Integer.parseInt(institudeId);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return PageInfo.convertPage2PageInfo(draw, courseRepository.searchCourse(start, length, id, sidx, sord));
 
    }

}