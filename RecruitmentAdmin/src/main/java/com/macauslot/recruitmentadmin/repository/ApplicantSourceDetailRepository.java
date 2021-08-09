package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.ApplicantSourceDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantSourceDetailRepository extends BaseRepository<ApplicantSourceDetail, Integer> {
    List<ApplicantSourceDetail> findAllByMessageIdOrderByOrderNum(Integer messageId);
    List<ApplicantSourceDetail> findAllByMessageIdOrderById(Integer messageId);

    List<ApplicantSourceDetail> findAllByMessageIdAndStatusOrderByOrderNum(Integer messageId,String status);

}
