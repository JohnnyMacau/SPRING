package com.macauslot.recruitment_ms.service.impl;

import com.macauslot.recruitment_ms.entity.ApplicantInfo;
import com.macauslot.recruitment_ms.exception.EmailException;
import com.macauslot.recruitment_ms.service.IEmailService;
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

    @Value("${contact_phoneNumbers}")
    private String[] contact_phoneNumbers;

    @Value("${spring.mail.sender}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, FreeMarkerConfigurer freeMarkerConfigurer, TaskExecutor taskExecutor) {
        this.mailSender = mailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.taskExecutor = taskExecutor;
    }

    //    @Override
    private void sendTemplateMail(ApplicantInfo data) throws EmailException {

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
                model.put("cn_name", "??????");
                model.put("en_name", "Mr.");
            } else if ("F".equals(gender)) {
                model.put("cn_name", "??????/??????");
                model.put("en_name", "Ms.");
            } else {
                model.put("cn_name", "??????/??????/??????");
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

            //?????? application.properties ????????????????????????
//            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//            configurer.setTemplateLoaderPath("classpath:templates");
            //?????? html ??????
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mail.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailException("email fail!");
        }
        mailSender.send(message);
    }


    @Override
    public void sendForgetPwdMail(ApplicantInfo data) throws EmailException {

        MimeMessage message;


        try {

            String gender = data.getGender();
            String cnLName = data.getCnLName();
            String enLName = data.getEnLName();
            String emailAddress = data.getEmailAddress();
//            String idCardNumber = data.getIdCardNumber();

            String temp =data.getPassword();
//            if (idCardNumber.length() <= 4) {
//                temp = idCardNumber;
//            } else {
////                temp=idCardNumber.substring(idCardNumber.length()-4); ?????????
//                temp = idCardNumber.substring(0, 4);//?????????
//            }


            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender, "Macau Slot HR");
            helper.setTo(emailAddress);
            helper.setSubject("???????????? - ????????????????????????  Reset your password ??? Macauslot.com");

            Map<String, String> model = new HashMap<>(20);

            if ("M".equals(gender)) {
                model.put("cn_name", "??????");
                model.put("en_name", "Mr.");
            } else if ("F".equals(gender)) {
                model.put("cn_name", "??????/??????");
                model.put("en_name", "Ms.");
            } else {
                model.put("cn_name", "??????/??????/??????");
                model.put("en_name", "Mr./Ms.");
            }
            if (StringUtils.isNotBlank(cnLName)) {
                model.put("cn_l_name", cnLName);
            } else {
                model.put("cn_l_name", "  ");
            }
            model.put("en_l_name", enLName);
            model.put("email_address", emailAddress);
            model.put("pwd", temp);


            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("forgetPwd.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailException("email fail!");
        }
        mailSender.send(message);
    }

//db??????: sendmail_OWL  ????????????????????????Y,???????????????;
    @Override
    public void sendCallNotFinishMail(ApplicantInfo data) throws EmailException {

        MimeMessage message;


        try {

            String gender = data.getGender();
            String cnLName = data.getCnLName();
            String enLName = data.getEnLName();
            String emailAddress = data.getEmailAddress();
//            String idCardNumber = data.getIdCardNumber();

//            String temp =data.getPassword();
//            if (idCardNumber.length() <= 4) {
//                temp = idCardNumber;
//            } else {
////                temp=idCardNumber.substring(idCardNumber.length()-4); ?????????
//                temp = idCardNumber.substring(0, 4);//?????????
//            }


            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender, "Macau Slot HR");
            helper.setTo(emailAddress);
            helper.setSubject("???????????? - ????????????????????????  Your application???s issue ??? Macauslot.com");

            Map<String, String> model = new HashMap<>(20);

            if ("M".equals(gender)) {
                model.put("cn_name", "??????");
                model.put("en_name", "Mr.");
            } else if ("F".equals(gender)) {
                model.put("cn_name", "??????/??????");
                model.put("en_name", "Ms.");
            } else {
                model.put("cn_name", "??????/??????/??????");
                model.put("en_name", "Mr./Ms.");
            }
            if (StringUtils.isNotBlank(cnLName)) {
                model.put("cn_l_name", cnLName);
            } else {
                model.put("cn_l_name", "  ");
            }
            model.put("en_l_name", enLName);
            model.put("email_address", emailAddress);
//            model.put("id_card_number", temp);

            model.put("contact_phoneNumbers", String.join(",", contact_phoneNumbers));


            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("callNotFinish.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailException("email fail!");
        }
        mailSender.send(message);
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void sendTemplateMailByAsynchronousMode(ApplicantInfo data, Consumer<ApplicantInfo> consumer) {
        taskExecutor.execute(() -> {


            try {
                sendTemplateMail(data);
                data.setEmailStatus("Y");
            } catch (Exception e) {
                System.err.println("-----sendMailByAsynchronousMode-----FAIL----");
                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                    sendTemplateMail(data);
                    data.setEmailStatus("Y");
                } catch (Exception e1) {
                    try {
                        Thread.sleep(10000);
                        sendTemplateMail(data);
                        data.setEmailStatus("Y");
                    } catch (Exception e2) {
                        System.err.println("-----sendMailByAsynchronousMode--FINAL--FAIL----");
                        data.setEmailStatus("N");
                    }
                }
            } finally {
                consumer.accept(data);
            }
        });
    }

    /**
     * ?????????????????????????????????
     */
    @Override
    public void sendCallNotFinishMailByAsynchronousMode(ApplicantInfo data, Consumer<ApplicantInfo> consumer) {
        taskExecutor.execute(() -> {
            try {
                sendCallNotFinishMail(data);
                data.setSendmailOwl("Y");
            } catch (Exception e) {
                System.err.println("-----sendCallNotFinishMail-----FAIL----");
                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                    sendCallNotFinishMail(data);
                    data.setSendmailOwl("Y");
                } catch (Exception e1) {
                    try {
                        Thread.sleep(10000);
                        sendCallNotFinishMail(data);
                        data.setSendmailOwl("Y");
                    } catch (Exception e2) {
                        System.err.println("-----sendCallNotFinishMail--FINAL--FAIL----");
                        data.setSendmailOwl("N");
                    }
                }
            } finally {
                consumer.accept(data);
            }
        });
    }

}
