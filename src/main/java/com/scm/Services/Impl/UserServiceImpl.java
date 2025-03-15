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
import com.scm.Helper.ResourceNotFoundException;
import com.scm.Repository.UserRepo;
import com.scm.Services.UserService;



@Service
public class UserServiceImpl implements UserService{

  @Autowired 
  private UserRepo userRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;



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
        return userRepo.save(user);
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


    


}
