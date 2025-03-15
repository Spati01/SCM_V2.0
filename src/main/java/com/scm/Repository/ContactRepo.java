package com.scm.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.Entitity.Contact;
import com.scm.Entitity.User;

@Repository
public interface ContactRepo extends  JpaRepository<Contact, String>{
 
    // find contact by user
    // Custom finder method 
  
    Page<Contact> findByUser(User user, Pageable pageable);

    List<Contact> findByUser(User user);
   
   // Custom Query method
    // find by user id 
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    // search by name
    Page<Contact> findByUserAndNameContaining(User user, String name, Pageable pageable);
    // search by phone number
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumber, Pageable pageable);
    // search by email address
    Page<Contact> findByUserAndEmailContaining(User user, String email, Pageable pageable);






   
   








}
