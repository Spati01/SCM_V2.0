package com.scm.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.scm.Entitity.User;
import com.scm.Helper.Helper;
import com.scm.Services.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider {


    private final UserService userService;


    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = Helper.getEmailOfLoggedInUser(authentication);
        return userService.getUserByEmail(username);
    }


    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Helper.getEmailOfLoggedInUser(authentication);
    }

    

}
