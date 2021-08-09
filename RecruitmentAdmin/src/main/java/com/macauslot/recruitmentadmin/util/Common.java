package com.macauslot.recruitmentadmin.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.macauslot.recruitmentadmin.aop.LoggingAspect;

public class Common {	
	
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {  
	   
    Class ownerClass = owner.getClass();  
  
    Class[] argsClass = new Class[args.length];  
  
    for (int i = 0, j = args.length; i < j; i++) {  
        argsClass[i] = args[i].getClass();  
    }  
 
     Method method = ownerClass.getMethod(methodName,argsClass);  
  
    return method.invoke(owner, args);  
	}
	

	public static int sendSMS(String mobile, String msg, String smsServiceUrl, String smsUser, String smsPassword){
		
		int result = -1;
//		msg = "HOICHON HOU,您好！您申請的職位軟件工程師安排於01/09/2021 00:00，在Meeting Room A 進行面試，敬請按時出席。"; //‘准’字發不了
		try{
			msg = URLEncoder.encode(msg,java.nio.charset.StandardCharsets.UTF_8.toString());
		}
		catch(Exception ex){
			logger.error(ex.toString(),ex);
		}
        String strPost = "UserID=" + smsUser + "&UserPassword=" + smsPassword + "&MessageType=TEXT&MessageLanguage=UTF8&MessageReceiver=" + mobile + "&MessageBody=" + msg;
        logger.info("post string:"+strPost);
        
        PostMethod post = null;
        InputStream stream = new ByteArrayInputStream(strPost.toString().getBytes(StandardCharsets.UTF_8));
	    post = new PostMethod(smsServiceUrl);
	    post.setRequestEntity(new InputStreamRequestEntity(stream));
	    post.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
	    HttpClient httpclient = new HttpClient();
	    try{
	    	httpclient.setConnectionTimeout(10000);
	        result = httpclient.executeMethod(post);
	    	String response = new String(post.getResponseBody(),StandardCharsets.UTF_8);
	    	logger.info("result code: " + result);
	    	logger.info("response: " + response);		
	    	System.out.println("response: " + response);		
	    }
		catch(Exception ex){
			logger.error(ex.toString(),ex);
		}    
	    return result;
	}
	
    public static void sendMail(JavaMailSender mailSender, String fromAddress, String[] mailAddress, String subject, String html) throws Exception {
    	MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress, "E-Recruitment System");
        helper.setTo(mailAddress);
        helper.setSubject(subject);

     
        helper.setText(html, true);
        mailSender.send(message);
}
}
