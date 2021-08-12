package com.school.repository;

import org.springframework.data.domain.Page;

import com.school.entity.Student;
import com.school.vo.StendentVO;

public interface StudentRepositoryCustom {

	 public Page<StendentVO> searchStudent(int start, int length, StendentVO condition, String sidx, String sord);
}
