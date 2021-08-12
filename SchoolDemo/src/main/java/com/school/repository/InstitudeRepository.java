package com.school.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.school.entity.Institude;

@Repository
public interface InstitudeRepository extends BaseRepository<Institude,Integer> {
	public List<Institude> findByUniversityId(int universityId);
}
