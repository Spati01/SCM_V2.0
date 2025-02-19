package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.Entitity.Providers;
import com.scm.Entitity.User;
import com.scm.Helper.AppConstants;
import com.scm.Repository.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenicationSuccessHandler implements AuthenticationSuccessHandler{

    private Logger logger =LoggerFactory.getLogger(OAuthAuthenicationSuccessHandler.class);
    
    @Autowired
    private UserRepo repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
           
           logger.info("Oauth2 Authentication Success");

        var oauth2 = (OAuth2AuthenticationToken)   authentication;

        String oauthId =  oauth2.getAuthorizedClientRegistrationId();

        logger.info("OAuth2 Authentication Id : " + oauthId);

       var oauthUser =  (DefaultOAuth2User) authentication.getPrincipal();

       oauthUser.getAttributes().forEach((k,v)->{
           logger.info(k + " : " + v);
       });

       User user = new User();
       user.setUserId(UUID.randomUUID().toString());
       user.setRollList(List.of(AppConstants.ROLE_USER));
       user.setEmailVerified(true);
         user.setEnabled(true);
         user.setPassword("dummy");
            user.setAbout("This account created by "+oauthId);
         

        if(oauthId.equalsIgnoreCase("google")) {
            //google
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setName(oauthUser.getAttribute("name").toString());   
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);





        }else if(oauthId.equalsIgnoreCase("github")) {

           // github

          String email = oauthUser.getAttribute("email") != null ? 
          oauthUser.getAttribute("email").toString() :
           oauthUser.getAttribute("login").toString()+"@gmail.com"; 

           String picture = oauthUser.getAttribute("avatar_url").toString();
           String name = oauthUser.getAttribute("login").toString();
           String providerId = oauthUser.getName();
           

           user.setEmail(email);
           user.setName(name);   
           user.setProfilePic(picture);
           user.setProviderUserId(providerId);
           user.setProvider(Providers.GITHUB);




        }else{

            logger.info("OAuthAuthenticationSuccessHandler : Unknow provider");
        }

           






           /*  

            DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
           
        


        String email  = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();
        

        // create user and save to database

        User data = new User();
        data.setEmail(email);
        data.setName(name);
        data.setProfilePic(picture);
        data.setUserId(UUID.randomUUID().toString());
        data.setProvider(Providers.GOOGLE);
        data.setEnabled(true);
        data.setEmailVerified(true);
        data.setProviderUserId(user.getName());
        data.setRollList(List.of(AppConstants.ROLE_USER));
       
        data.setAbout("This account created by Google");
          
         repo.findByEmail(email).ifPresentOrElse(u->{
            logger.info("User already exist" + u.toString());
        },()->{
            logger.info("User not exist");
            repo.save(data);
            logger.info("User created successfully : " + data.toString());
        });


      
       */



       User user_data = repo.findByEmail(user.getEmail()).orElse(null);
       if(user_data == null) {
           repo.save(user);
           logger.info("User created successfully : " + user.toString());
       }else{
           logger.info("User already exist" + user_data.toString());
       }

          new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

    }

}
