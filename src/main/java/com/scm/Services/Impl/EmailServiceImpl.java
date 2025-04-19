package com.scm.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.Services.EmailService;


@Service
public class EmailServiceImpl  implements  EmailService{


    @Autowired
    private JavaMailSender  javaMailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Override
    public void sendEmail(String to, String sunject, String body) {
      
     SimpleMailMessage message = new SimpleMailMessage();
    
     message.setTo(to);
     message.setSubject(sunject);
     message.setText(body);
     message.setFrom(domainName);

     javaMailSender.send(message);

    }

    @Override
    public void sendEmailWithHtml() {
     
    }

    @Override
    public void sendEmailWithAttachment() {
     
    }


    
}
