package com.school.repository;

import org.springframework.stereotype.Repository;

import com.school.entity.Course;

@Repository
public interface CourseRepository extends BaseRepository<Course,Integer>,CourseRepositoryCustom {
	
}
