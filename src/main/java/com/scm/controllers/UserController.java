package com.scm.controllers;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;




@Controller
@RequestMapping("/user")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);
   
  @Autowired
  private UserService service;





    // User dashbaord
 @RequestMapping(value = "/dashboard")
 public String userDashbaord() {

    System.out.println("Dashboard page is running.....");
     return"user/dashboard";
 }

 // User profile page
 @RequestMapping(value = "/profile")
 public String userProfile(Authentication authentication, Model model) {


    return "user/profile";
 }
 
  // User add contact page

    // user view contact page


  // User edit contact page

  // User delete contact page

 



}
