package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends BaseRepository<Language, Integer> {

    List<Language> findLanguageByApplicantInfoIdOrderByApplicantInfoId(Integer applicantInfoId);


}
