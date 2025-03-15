package com.scm.Services.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.Entitity.Contact;
import com.scm.Entitity.User;
import com.scm.Repository.ContactRepo;
import com.scm.Services.ContactService;
import com.scm.Services.ImageService;
import com.scm.Services.UserService;
import com.scm.config.AuthenticatedUserProvider;
import com.scm.forms.ContactForm;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ContactServiceImpl implements  ContactService{

    @Autowired
    private ContactRepo repo;

    @Autowired
    private UserService userService;
   

    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthenticatedUserProvider userProvider;
  

    @Override
    public Contact saveContact(ContactForm contact) {

        //  Get Logged-in User Directly
        User user = userProvider.getLoggedInUser();

        //  Generate Image Name
        String filename = UUID.randomUUID().toString();

        // Upload Image to Cloudinary
        String file_url = imageService.uploadImage(contact.getContactImage(), filename);

        log.info("This is image name : {}", file_url);

        
        Contact contact_data = new Contact();
        contact_data.setId(UUID.randomUUID().toString());
        contact_data.setName(contact.getName());
        contact_data.setEmail(contact.getEmail());
        contact_data.setPhoneNumber(contact.getPhoneNumber());
        contact_data.setAddress(contact.getAddress());
        contact_data.setWebsiteLink(contact.getWebsite());
        contact_data.setDescription(contact.getDescription());
        contact_data.setLinkedinLink(contact.getLinkedIn());
        contact_data.setPicture(file_url);
        contact_data.setCloudinaryImagePublicId(filename);
        contact_data.setFavorite(contact.isFavorite());
        contact_data.setUser(user);

       
        return repo.save(contact_data);
    }

    @Override
    public void updateContact(Contact contact) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    }

    @Override
    public List<Contact> getAllContact() {
       
        return repo.findAll();
    
    }

    @Override
    public Contact getById(String id) {
     
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found given id:" + id));

    }

    @Override
    public void deleteContact(String id) {
      var contact =   repo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found given id:" + id));
       repo.delete(contact);
    }

  

    @Override
    public List<Contact> getByUserId(String userId) {

        return repo.findByUserId(userId);
    }

    @Override
    public List<Contact> getByUser(User user) {
        return repo.findByUser(user);
      
    }

    @Override
    public Page<Contact> getContactsByLoggedInUser(int page, int size, String sortBy, String direction) {
        User LoggedInUser = userProvider.getLoggedInUser();

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);
       

        return repo.findByUser(LoggedInUser,pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String direction) {
        User LoggedInUser = userProvider.getLoggedInUser();
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
       
        return repo.findByUserAndNameContaining(LoggedInUser, name,  pageable);

}

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String direction) {
        User LoggedInUser = userProvider.getLoggedInUser();
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByUserAndPhoneNumberContaining(LoggedInUser, phoneNumber,  pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String direction) {
        User LoggedInUser = userProvider.getLoggedInUser();
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByUserAndEmailContaining(LoggedInUser, email, pageable);
    }
}