package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends BaseRepository<Language, Integer> {

    List<Language> findLanguageByApplicantInfoIdOrderByApplicantInfoId(Integer applicantInfoId);


}
