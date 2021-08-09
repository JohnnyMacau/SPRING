package com.macauslot.recruitmentadmin.controller;

import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.DateBetweenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("/Service")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final RecruitmentService recruitmentService;
    @Resource
    private Environment env;

    @Value("${special.treatment.blacklist.reasons}")
    private String[] blacklistReasons;

    @Autowired
    public JobController(JavaMailSender mailSender, FreeMarkerConfigurer freeMarkerConfigurer, TaskExecutor taskExecutor, RecruitmentService recruitmentService) {
        this.mailSender = mailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.recruitmentService = recruitmentService;
    }

    @Bean(name = "TaskPool")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }

    /**
     * 黑名單原因“暫時”， 以離職日期計算一年之後自動失效，(*没離職日期的,則不會自動失效)  每日凌晨3時執行一次
     */
    @Scheduled(cron = "0 0 3 * * ?")
//    @Scheduled(cron = "30 * * * * ?")// test 每分鐘第30秒執行一次
    public void checkBlacklist() {
        if (blacklistReasons == null || blacklistReasons.length == 0) {
            return;
        }
        logger.info("checkBlacklist job starting...");
        try {
            Date date = DateBetweenUtil.getDateByCondition(new Date(), -1, Calendar.YEAR);
            int count = recruitmentService.scheduledBlacklist(DateBetweenUtil.convertDateToStr(date), Arrays.asList(blacklistReasons));
            if (count > 0) {
                logger.info("checkBlacklist job update data count: " + count);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            e.printStackTrace();
        }
        logger.info("checkBlacklist job ended.");
    }

//    @Scheduled(fixedDelay = 1000 * 60 * 1)
//    public void sendMail() {
//        if (env.getProperty("job.mail").compareTo("Y") != 0) return;
//
//        logger.info("Sending mail...");
//        try {
//
//        } catch (Exception e) {
//            logger.info(e.toString(), e);
//            e.printStackTrace();
//        }
//        logger.info("Send mail job ended.");
//    }

    public void sendMail(String[] mailAddress, String subject, String html) throws Exception {
        String testMailAddress = env.getProperty("spring.mail.test.address");
        if (testMailAddress != null && testMailAddress.compareTo("") != 0) {
            mailAddress = new String[]{testMailAddress};
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(env.getProperty("spring.mail.sender"), "E-Appraisal System");
        helper.setTo(mailAddress);
        helper.setSubject(subject);


        helper.setText(html, true);
        mailSender.send(message);
    }

    @RequestMapping("/testMail")
    public @ResponseBody
    String testMail(HttpServletRequest request) {
        try {
            String mailAddress = "johnny.kou@macauslot.com";
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(env.getProperty("spring.mail.sender"), "E-Recruitment System");
            helper.setTo(mailAddress);
            helper.setSubject("Test Mail From E-Recruitment System");
            helper.setText("This is a test mail from E-Recruitment System", true);
            mailSender.send(message);
            return "Mail sent successfully!";
        } catch (Exception e) {
            return "Exception occurred when sending mail!";
        }
    }
}
