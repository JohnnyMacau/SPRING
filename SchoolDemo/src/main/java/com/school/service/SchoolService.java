package com.school.service;

import java.util.List;

import com.school.entity.Users;

public interface SchoolService {
    public List<Users> getUsers(String username, String password);

    public void batchDeleteStudent(Integer[] idList);
}
