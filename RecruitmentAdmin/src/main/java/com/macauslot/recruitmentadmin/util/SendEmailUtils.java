package com.macauslot.recruitmentadmin.util;

import com.macauslot.recruitmentadmin.vo.EmailParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件工具类
 * @author
 *
 */
@Component
public class SendEmailUtils {


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public SendEmailUtils(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * html模板邮件
     * @param from 发件人
     * @param to 收件人
     * @param subject 邮件主题
     * @param emailParam 给模板的参数
     * @param template html模板路径(相对路径)  Thymeleaf的默认配置期望所有HTML文件都放在 **resources/templates ** 目录下，以.html扩展名结尾。
     * @param imgPath 图片/文件路径(绝对路径)
     * @throws MessagingException
     */
    public void thymeleafEmail(String from, String[] to, String subject, EmailParamVO emailParam, String template, String imgPath) throws MessagingException {
        MimeMessage mimeMessage =javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        // 利用 Thymeleaf 模板构建 html 文本
        Context ctx = new Context();
        // 给模板的参数的上下文
        ctx.setVariable("emailParam", emailParam);
        // 执行模板引擎，执行模板引擎需要传入模板名、上下文对象
        // Thymeleaf的默认配置期望所有HTML文件都放在 **resources/templates ** 目录下，以.html扩展名结尾。
        // String emailText = templateEngine.process("email/templates", ctx);
        String emailText = templateEngine.process(template, ctx);
        mimeMessageHelper.setText(emailText, true);
        // FileSystemResource logoImage= new FileSystemResource("D:\\image\\logo.jpg");
        //绝对路径
/*
        FileSystemResource logoImage = new FileSystemResource(imgPath);
*/
        //相对路径，项目的resources路径下
        //ClassPathResource logoImage = new ClassPathResource("static/image/logonew.png");
        // 添加附件,第一个参数表示添加到 Email 中附件的名称，第二个参数是图片资源
        //一般图片调用这个方法
/*
        mimeMessageHelper.addInline("logoImage", logoImage);
*/
        //一般文件附件调用这个方法
//		mimeMessageHelper.addAttachment("logoImage", resource);
        javaMailSender.send(mimeMessage);

    }
}
