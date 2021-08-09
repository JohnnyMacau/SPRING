package com.macauslot.recruitmentadmin.service;


import com.macauslot.recruitmentadmin.entity.ApplicantInfo;


import java.util.function.Consumer;

public interface IEmailService {



//    void sendTemplateMail(ApplicantInfo data)throws EmailException;

/*
    void sendForgetPwdMail(ApplicantInfo data) throws EmailException;
*/


    void sendTemplateMailByAsynchronousMode(ApplicantInfo data, Consumer<ApplicantInfo> consumer);
}
