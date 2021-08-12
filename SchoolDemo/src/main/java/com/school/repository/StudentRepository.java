package com.school.repository;

import org.springframework.stereotype.Repository;

import com.school.entity.Student;

@Repository
public interface StudentRepository extends BaseRepository<Student,Integer>,StudentRepositoryCustom {
	
}
