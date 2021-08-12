package com.school.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.school.entity.Users;
import com.school.vo.UserVO;

public interface UserRepositoryCustom {

	 public List<Users> getUsers(String username, String password);
	 public Page<Users> searchUser(int start, int length, UserVO condition, String sidx, String sord);
}
