package com.demo.repository;

import org.springframework.stereotype.Repository;

import com.demo.entity.Users;

@Repository
public interface UserRepository extends BaseRepository<Users,Integer>,UserRepositoryCustom {

}
