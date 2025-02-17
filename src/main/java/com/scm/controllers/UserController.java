package com.scm.controllers;

import java.security.Principal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/user")
public class UserController {

  private Logger logger = LoggerFactory.getLogger(UserController.class);


    // User dashbaord
 @RequestMapping(value = "/dashboard")
 public String userDashbaord() {

    System.out.println("Dashboard page is running.....");
     return"user/dashboard";
 }

 // User profile page
 @RequestMapping(value = "/profile")
 public String userProfile(Principal principal) {
  String name = principal.getName();
  logger.info(("User logged in : {}" + name));
     System.out.println("User Profile");
    return "user/profile";
 }
 
  // User add contact page

    // user view contact page


  // User edit contact page

  // User delete contact page

 



}
