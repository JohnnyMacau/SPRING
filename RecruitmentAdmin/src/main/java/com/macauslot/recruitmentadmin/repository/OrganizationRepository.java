package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.entity.Organization;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, Integer> {


    List<Organization> findAllByOrderByOrgId();


}
