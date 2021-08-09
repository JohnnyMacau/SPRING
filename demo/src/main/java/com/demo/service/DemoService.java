package com.demo.service;

import java.util.List;

import com.demo.entity.Users;

public interface DemoService {
    public List<Users> getUsers(String username, String password);
}
