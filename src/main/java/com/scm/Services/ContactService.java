package com.scm.Services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.scm.Entitity.Contact;
import com.scm.Entitity.User;
import com.scm.forms.ContactForm;

public interface ContactService {
  

    // save
    Contact saveContact(ContactForm contact);
   // update Contact
    void updateContact(Contact contact);
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
}
