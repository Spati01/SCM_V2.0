package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.Services.EmailService;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private EmailService emailService;

	@Test
	void sendEmailTest(){
  emailService.sendEmail("subhadippati30@gmail.com",
                          "Testing managing  email services", 
						  "This is SCM project working on email services");
	}
}
