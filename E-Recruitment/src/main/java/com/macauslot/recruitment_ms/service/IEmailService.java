package com.macauslot.recruitment_ms.service;

import com.macauslot.recruitment_ms.entity.ApplicantInfo;
import com.macauslot.recruitment_ms.exception.EmailException;

import java.util.function.Consumer;

public interface IEmailService {



//    void sendTemplateMail(ApplicantInfo data)throws EmailException;

    void sendForgetPwdMail(ApplicantInfo data) throws EmailException;


    void sendCallNotFinishMail(ApplicantInfo data) throws EmailException;

    void sendTemplateMailByAsynchronousMode(ApplicantInfo data, Consumer<ApplicantInfo> consumer);

    void sendCallNotFinishMailByAsynchronousMode(ApplicantInfo data, Consumer<ApplicantInfo> consumer);
}
