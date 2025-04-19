package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.Entitity.ActivityLog;
import com.scm.Entitity.ActivityLog.ActionType;
import com.scm.Entitity.Contact;
import com.scm.Entitity.User;
import com.scm.Helper.Helper;
import com.scm.Services.ActivityLogService;
import com.scm.Services.ContactService;
import com.scm.Services.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {

    // Get contact of user
     @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private ContactService contactService;

    

    @GetMapping("/contacts/{contactId}")
    public Contact geContact(@PathVariable String contactId) {
     Contact contact = contactService.getById(contactId);
    
     activityLogService.logActivity(contact.getUser(), 
     contact.getName(), 
     ActivityLog.ActionType.VIEWED);
      return contact;
    }

}
