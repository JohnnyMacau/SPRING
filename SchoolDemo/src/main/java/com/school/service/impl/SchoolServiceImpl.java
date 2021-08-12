package com.school.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.entity.Users;
import com.school.repository.StudentRepository;
import com.school.repository.UserRepository;
import com.school.service.AbstractBaseService;
import com.school.service.SchoolService;
@Service
public class SchoolServiceImpl extends AbstractBaseService implements SchoolService {


//    @Value("${sms.service.url}")
//    private String smsServiceUrl;



    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public SchoolServiceImpl(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public List<Users> getUsers(String username, String password) {
        return userRepository.getUsers(username, password);
    }

    
    @Override
    public void batchDeleteStudent(Integer[] idList){
        for (Integer id : idList) {
        	studentRepository.deleteById(id);
        }
    }
}
