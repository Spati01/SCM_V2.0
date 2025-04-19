package com.scm.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.Entitity.User;
import com.scm.Helper.AppConstants;
import com.scm.Helper.Helper;
import com.scm.Helper.ResourceNotFoundException;
import com.scm.Repository.ContactRepo;
import com.scm.Repository.UserRepo;
import com.scm.Services.EmailService;
import com.scm.Services.UserService;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserServiceImpl implements UserService{

  @Autowired 
  private UserRepo userRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;


  @Autowired
  private ContactRepo contactRepo;



   private Logger logger = LoggerFactory.getLogger(this.getClass());
    
  


    @Override
    public User saveUser(User user) {
        // USer id : Have to generate
        String userId =  UUID.randomUUID().toString();
        user.setUserId(userId);
   //Password Encode
      user.setPassword(passwordEncoder.encode(user.getPassword()));

   // set role
    user.setRollList(List.of(AppConstants.ROLE_USER));


   logger.info(user.getProvider().toString());
       

        // send email verification link

        String emailToken = UUID.randomUUID().toString();
         user.setEmailToken(emailToken);
         User saveUser =  userRepo.save(user);
        String emailLink = Helper.getLinkForEmailVerification(emailToken);
        logger.info("Email Link : "+emailLink);
        emailService.sendEmail(
          saveUser.getEmail(), 
          "✅ Verify Your Email", 
          "Hi,\n\n" +
          "Click the link below to verify your email and activate your account:\n\n" +
          emailLink + "\n\n" +
          "🔒 Stay secure!\n\n" +
          "Best,\nYour SCMS Team"
      );
      

        return saveUser;
    }




    @Override
    public Optional<User> getUserById(String id) {
      return userRepo.findById(id);

    }

    @Override
    public Optional<User> updateUser(User user) {
        
    User newUser = userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("Uaer Not Found"));
    //Update newUser form user
  newUser.setName(user.getName());
  newUser.setEmail(user.getEmail());
  newUser.setPhoneNumber(user.getPhoneNumber());
  newUser.setPassword(user.getPassword());
  newUser.setAbout(user.getAbout());
  newUser.setProfilePic(user.getProfilePic());
newUser.setEnabled(user.isEnabled());
newUser.setEmailVerified(user.isEmailVerified());
newUser.setPhoneVerified(user.isPhoneVerified());
newUser.setProvider(user.getProvider());
newUser.setProviderUserId(user.getProviderUserId());

   User save =  userRepo.save(newUser);
   return Optional.ofNullable(save);


    }

    @Override
    public void deleteUser(String id) {

        User newUser = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Uaer Not Found"));
        
         userRepo.delete(newUser);
 
    }

    @Override
    public boolean isUserExist(String userId) {
       User newUser = userRepo.findById(userId).orElse(null);
       return newUser != null ? true : false;

    }

    @Override
    public boolean isUserExistByEmail(String email) {
           
     User user = userRepo.findByEmail(email).orElse(null);
     return user != null ? true : false;


    }

    @Override
    public List<User> getAllUser() {
      return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
     return userRepo.findByEmail(email).orElse(null);

    }




    @Override
    public User getByEmailToken(String token) {
      return userRepo.findByEmailToken(token).orElse(null);
    }




    @Override
    public boolean verifyEmailToken(String token) {
     

      log.info("Verifying email token: {}", token);

        User user = getByEmailToken(token);
        if (user == null) {
            log.error("Invalid email token: {}", token);
            return false;
        }

        if (user.isEmailVerified()) {
            log.warn("User email already verified: {}", user.getEmail());
            return false;
        }

        user.setEmailVerified(true);
        user.setEnabled(true);
        userRepo.save(user);
        log.info("User email verification successful for: {}", user.getEmail());
        return true;
    }




    


 
    

}
