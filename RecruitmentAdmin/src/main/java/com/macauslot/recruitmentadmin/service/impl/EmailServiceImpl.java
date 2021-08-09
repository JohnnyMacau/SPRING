package com.macauslot.recruitmentadmin.service.impl;


import com.macauslot.recruitmentadmin.entity.ApplicantInfo;
import com.macauslot.recruitmentadmin.exception.SendEmailException;
import com.macauslot.recruitmentadmin.service.IEmailService;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author jim.deng
 */
@Service
public class EmailServiceImpl implements IEmailService {


    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final TaskExecutor taskExecutor;


    @Value("${spring.mail.sender}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, FreeMarkerConfigurer freeMarkerConfigurer, TaskExecutor taskExecutor) {
        this.mailSender = mailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.taskExecutor = taskExecutor;
    }

    private void sendSuccessfulRegistrationMail4Flt(ApplicantInfo data) throws SendEmailException {

        MimeMessage message;
        try {

            String gender = data.getGender();
            String cnLName = data.getCnLName();
            String enLName = data.getEnLName();
            String emailAddress = data.getEmailAddress();
            String pwd = data.getPassword();


            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender, "Goalslot HR");
            helper.setTo(emailAddress);
            helper.setSubject("Thank you for your application - Goalslot");

            Map<String, String> model = new HashMap<>(20);

            if ("M".equals(gender)) {
                model.put("cn_name", "先生");
                model.put("en_name", "Mr.");
            } else if ("F".equals(gender)) {
                model.put("cn_name", "小姐/女士");
                model.put("en_name", "Ms.");
            } else {
                model.put("cn_name", "先生/小姐/女士");
                model.put("en_name", "Mr./Ms.");
            }
            if (StringUtils.isNotBlank(cnLName)) {
                model.put("cn_l_name", cnLName);
            } else {
                model.put("cn_l_name", "  ");
            }
            model.put("en_l_name", enLName);
            model.put("email_address", emailAddress);
            model.put("pwd", pwd);

            //修改 application.properties 文件中的读取路径
/*            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
            configurer.setTemplateLoaderPath("classpath:/email/");*/
            //读取 html 模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/email/successful_registration_mail_flt.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendEmailException("email fail!");
        }
        mailSender.send(message);
    }

    private void sendSuccessfulRegistrationMail4Mslot(ApplicantInfo data) throws SendEmailException {

        MimeMessage message;
        try {

            String gender = data.getGender();
            String cnLName = data.getCnLName();
            String enLName = data.getEnLName();
            String emailAddress = data.getEmailAddress();
            String pwd = data.getPassword();


            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender, "Macau Slot HR");
            helper.setTo(emailAddress);
            helper.setSubject("Thank you for your application - Macauslot.com");

            Map<String, String> model = new HashMap<>(20);

            if ("M".equals(gender)) {
                model.put("cn_name", "先生");
                model.put("en_name", "Mr.");
            } else if ("F".equals(gender)) {
                model.put("cn_name", "小姐/女士");
                model.put("en_name", "Ms.");
            } else {
                model.put("cn_name", "先生/小姐/女士");
                model.put("en_name", "Mr./Ms.");
            }
            if (StringUtils.isNotBlank(cnLName)) {
                model.put("cn_l_name", cnLName);
            } else {
                model.put("cn_l_name", "  ");
            }
            model.put("en_l_name", enLName);
            model.put("email_address", emailAddress);
            model.put("pwd", pwd);

            //修改 application.properties 文件中的读取路径
//            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//            configurer.setTemplateLoaderPath("classpath:templates");
            //读取 html 模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/email/successful_registration_mail_mslot.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendEmailException("email fail!");
        }
        mailSender.send(message);
    }

    private void checkStyle(ApplicantInfo data) {
        if ("FLT".equalsIgnoreCase(data.getApplicationOrg())) {
            sendSuccessfulRegistrationMail4Flt(data);
        } else if ("SLT".equalsIgnoreCase(data.getApplicationOrg())) {
            sendSuccessfulRegistrationMail4Mslot(data);
        } else {
            throw new IllegalArgumentException("sendEmail ApplicationOrg error");
        }
    }


    /**
     * 异步发送邮件
     */
    @Override
    public void sendTemplateMailByAsynchronousMode(ApplicantInfo data, Consumer<ApplicantInfo> consumer) {
        taskExecutor.execute(() -> {
            try {
                checkStyle(data);
                data.setEmailStatus("Y");
            } catch (Exception e) {
                System.err.println("-----sendMailByAsynchronousMode-----FAIL----");
//                e.printStackTrace();
//                try {
//                    Thread.sleep(10000);
//                    checkStyle(data);
//                    data.setEmailStatus("Y");
//                } catch (Exception e1) {
//                    try {
//                        Thread.sleep(10000);
//                        checkStyle(data);
//                        data.setEmailStatus("Y");
//                    } catch (Exception e2) {
//                        System.err.println("-----sendMailByAsynchronousMode--FINAL--FAIL----");
                        data.setEmailStatus("N");
//                    }
//                }
            } finally {
                consumer.accept(data);
            }
        });
    }


}
