package com.macauslot.recruitmentadmin.service;


import com.macauslot.recruitmentadmin.dto.UserPO;
import com.macauslot.recruitmentadmin.exception.UserDataMissingException;
import com.macauslot.recruitmentadmin.exception.UserOrPasswordNotMatchException;


public interface IUserService {

    UserPO login(String username, String password) throws
            UserOrPasswordNotMatchException,
            UserDataMissingException;
}
