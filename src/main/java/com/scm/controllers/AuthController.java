package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Services.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {


 @Autowired
private UserService userService;

@GetMapping("/verify-email")
public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
    // Verify the email token and update user's email verification status

    log.info("Received request to verify email with token: {}", token);

    boolean isVerified = userService.verifyEmailToken(token);

    if (isVerified) {
        log.info("Email verification successful for token: {}", token);

             // Add error message to the session
    session.setAttribute("message", Message.builder()
    .content("🟢 Your email has been verified! Now you can log in. ✅")
    .type(MessageType.blue)
    .build());
  
        return "success_page";
    } else {
        log.error("Email verification failed for token: {}", token);

           // Add error message to the session
    session.setAttribute("message", Message.builder()
    .content("❌ Your email is not verified! Please try again later.🔴")
    .type(MessageType.red)
    .build());
  
    return "error_page";
    }

}
 


}
