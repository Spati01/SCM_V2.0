package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/user")
public class UserController {


    // User dashbaord
 @RequestMapping(value = "/dashboard")
 public String userDashbaord() {

    System.out.println("Dashboard page is running.....");
     return"user/dashboard";
 }

 // User profile page
 @RequestMapping(value = "/profile")
 public String userProfile() {
     System.out.println("User Profile");
    return "user/profile";
 }
 
  // User add contact page

    // user view contact page


  // User edit contact page

  // User delete contact page

 



}
