package com.scm.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.scm.Entitity.Contact;
import com.scm.Entitity.User;
import com.scm.forms.ContactForm;
import com.scm.forms.DayData;

public interface ContactService {
  

    // save
    Contact saveContact(ContactForm contact);
   // update Contact
    Contact updateContact(Contact contact);

//  update contact form view
    ContactForm updateContactForm(String id);



    // get contact
    List<Contact> getAllContact();
    
    // get by id 
    Contact getById(String id);

    // delete contact
    void deleteContact(String id);
     
     //  search by name or phone number
    Page<Contact> searchByName(String name, int page, int size,String sortBy, String direction);
    Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size,String sortBy, String direction);
    Page<Contact> searchByEmail(String email, int page, int size,String sortBy, String direction);




    
    // get contact by user id
    List<Contact> getByUserId(String userId);

    List<Contact> getByUser(User user);

    Page<Contact> getContactsByLoggedInUser(int page, int size,String sortBy, String direction); 

    long getContactCountForUser(String email);

     Map<String, DayData> getWeeklyAnalytics(String email);

     double getWeekChange(String email);

     public List<Contact> getAllContactsForUser(String email);

     public long getFavoriteContactCountForUser(String email);


     public long getUnFavoriteContactCountForUser(String email);

  
}
