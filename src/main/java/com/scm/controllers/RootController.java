package com.scm.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.Entitity.User;
import com.scm.Helper.Helper;
import com.scm.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RootController {

    private final Logger logger = LoggerFactory.getLogger(RootController.class);
    
    @Autowired
    private UserService service;
    
  @ModelAttribute
  public void addLoggedInuserInformation(Model model, Authentication authentication, HttpServletRequest request) {
       
    if(authentication == null) {
        logger.info("User is not logged in");
        return;
    }
    
    System.out.println("Adding logged in user information to the model");
         String username = Helper.getEmailOfLoggedInUser(authentication);
         logger.info("Extracted email from authentication: {}", username);
          logger.info(("User logged in : {}" + username));
          User user= service.getUserByEmail(username);
            System.out.println("User " + user);
                System.out.println(user.getName());
                System.out.println(user.getEmail());
                model.addAttribute("LoggedInUser", user);

                
                model.addAttribute("currentPage", request.getRequestURI());

          
         
      // get user data from database
          
  }


}
