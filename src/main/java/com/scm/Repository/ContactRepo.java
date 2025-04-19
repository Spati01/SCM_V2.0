package com.scm.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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



    long countByUserEmail(String email);

    long countByUserEmailAndFavoriteTrue(String email);
  
    long countByUserEmailAndFavoriteFalse(String email);
    
    @Query("SELECT c FROM Contact c WHERE c.user.email = :email AND c.createdAt BETWEEN :start AND :end")
    List<Contact> findContactsByUserEmailAndCreatedAtBetween(@Param("email") String email, 
                                                              @Param("start") LocalDateTime start, 
                                                              @Param("end") LocalDateTime end);
    

    // Order contacts by createdAt in descending order by user email
    @Query("SELECT c FROM Contact c WHERE c.user.email = :email ORDER BY c.createdAt DESC")
    List<Contact> findByUserEmailOrderByCreatedAtDesc(@Param("email") String email);



@Query("SELECT COUNT(c) FROM Contact c WHERE c.user.id = :userId AND DATE(c.createdAt) = :date")
int countByUserAndDate(@Param("userId") String userId, @Param("date") LocalDate date);


}
