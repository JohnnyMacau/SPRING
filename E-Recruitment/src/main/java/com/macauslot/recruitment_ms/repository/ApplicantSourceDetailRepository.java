package com.macauslot.recruitment_ms.repository;



import com.macauslot.recruitment_ms.entity.ApplicantSourceDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantSourceDetailRepository extends BaseRepository<ApplicantSourceDetail, Integer> {
    List<ApplicantSourceDetail> findAllByMessageIdAndStatusOrderByOrderNum(Integer messageId,String status);

}
