package com.school.repository;

import org.springframework.data.domain.Page;

import com.school.entity.Course;

public interface CourseRepositoryCustom {

	 public Page<Course> searchCourse(int start, int length, int institudeId, String sidx, String sord);
}
