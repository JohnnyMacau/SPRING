package com.school.repository;

import org.springframework.stereotype.Repository;

import com.school.entity.Users;

@Repository
public interface UserRepository extends BaseRepository<Users,Integer>,UserRepositoryCustom {

}
