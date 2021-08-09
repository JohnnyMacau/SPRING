package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.Message;

import java.util.List;

import org.springframework.data.domain.Page;

public interface MessageRepositoryCustom {


    Page<Message> page4ApplicantSourceType(int start, int length, Message condition, String sidx, String sord);
    public List<Message> getReturnStatusList(int currentStage);
    public List<Message> getProcessStatusList();
//    public List<Message> getBlackListReasonList();
}
