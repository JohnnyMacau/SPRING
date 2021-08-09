package com.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.Users;
import com.demo.repository.UserRepository;
import com.demo.service.AbstractBaseService;
import com.demo.service.DemoService;
@Service
public class DemoServiceImpl extends AbstractBaseService implements DemoService {


//    @Value("${sms.service.url}")
//    private String smsServiceUrl;



    private final UserRepository userRepository;

    @Autowired
    public DemoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<Users> getUsers(String username, String password) {
        return userRepository.getUsers(username, password);
    }

}
