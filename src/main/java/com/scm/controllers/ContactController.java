package com.scm.controllers;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.Entitity.ActivityLog;
import com.scm.Entitity.Contact;
import com.scm.Entitity.User;
import com.scm.Helper.AppConstants;
import com.scm.Helper.Helper;
import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Repository.ActivityLogRepository;
import com.scm.Services.ActivityLogService;
import com.scm.Services.ContactService;
import com.scm.Services.ImageService;
import com.scm.Services.UserService;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;






@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {




    @Autowired 
    private ContactService contactService;

    @Autowired
    private ImageService imageService;


   @Autowired
  private ActivityLogService activityLogService;
  

    @Autowired
    private UserService userService;


    // add contact
    @RequestMapping("/add_contact")
    public String addContact(Model model) {

        log.info("Adding a new contact");

         ContactForm contact =  new ContactForm();
         
         contact.setFavorite(true);

        model.addAttribute("contactForm", contact);
        
        return "user/add_contact";
    }

    // Save Contacts
    @PostMapping("/add_contact")
    public String postMethodName(@Valid  @ModelAttribute ContactForm contactForm, 
                                BindingResult result, HttpSession session) {


  // Valid form 

     if(result.hasErrors()) {
        session.setAttribute("message", Message.builder()
        .content("Please correct the following errors")
        .type(MessageType.red)
        .build());
        return "user/add_contact";
     }





        // Process contact Form data
        contactService.saveContact(contactForm);
      log.info("Processing contact Form : {}", contactForm);

      session.setAttribute("message",  Message.builder()
      .content("You have successfully added a new contact")
      .type(MessageType.red)
      .build());


    
        return "redirect:/user/contacts/add_contact";
    }
    
    // show contacts
    @GetMapping("/view_contact")
    public String viewContact(
        @RequestParam(value = "page", defaultValue= "0") int page,
        @RequestParam(value = "size", defaultValue= AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "sortBy" , defaultValue= "name") String sortBy,
        @RequestParam(value = "direction", defaultValue= "asc") String direction,
        Authentication authentication, Model model) {

       Page<Contact> pageContacts = contactService.getContactsByLoggedInUser(page, size, sortBy, direction);
        log.info("pageContacts : {}", pageContacts);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
       
        

        return "user/view_contacts";
    }



    


  
// Search contacts
@GetMapping("/search_contacts")
public String searchContacts(
    @ModelAttribute ContactSearchForm contactSearchForm,
    @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE + "") int size,
    @RequestParam(value="page", defaultValue="0") int page,
    @RequestParam(value="sortBy", defaultValue="name") String sortBy,
    @RequestParam(value="direction", defaultValue="asc") String direction,
    Model model
) {
    log.info("Searching contacts for : {} - {}", contactSearchForm.getField(), contactSearchForm.getValue());

    Page<Contact> pageContact = Page.empty(); // Default empty result

    // Separate error messages for each field
    if (contactSearchForm.getField() == null || contactSearchForm.getField().trim().isEmpty()) {
        model.addAttribute("fieldError", "Please select a search field.");
    } else if (contactSearchForm.getValue() == null || contactSearchForm.getValue().trim().isEmpty()) {
        model.addAttribute("valueError", "Please enter a search term.");
    } else {
        switch (contactSearchForm.getField().toLowerCase()) {
            case "name" -> pageContact = contactService.searchByName(contactSearchForm.getValue(), page, size, sortBy, direction);
            case "email" -> pageContact = contactService.searchByEmail(contactSearchForm.getValue(), page, size, sortBy, direction);
            case "phone" -> pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), page, size, sortBy, direction);
            default -> model.addAttribute("fieldError", "Invalid search field selected.");
        }
    }

    log.info("Search results: {}", pageContact.getTotalElements());

    model.addAttribute("contactSearchForm", contactSearchForm);
    model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
    model.addAttribute("pageContacts", pageContact);

    return "user/search_contacts";
}





     // Delete contact
    @GetMapping("/delete_contact/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {
        log.info("Deleting contact with id : {}", contactId);
        
        contactService.deleteContact(contactId);

        session.setAttribute("message", Message.builder()
        .content("You have successfully deleted the contact")
        .type(MessageType.green)
        .build());
        return "redirect:/user/contacts/view_contact";
    }



    // Edit contact
    @GetMapping("/edit_contact/{contactId}")
    public String editContact(@PathVariable("contactId") String contactId, Model model) {
        log.info("Editing contact with id : {}", contactId);

        ContactForm contactForm = contactService.updateContactForm(contactId);
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);


     return "user/update_contact";

}


// process update contact form
@PostMapping("/update_contact/{contactId}")
public String updateContact(@PathVariable("contactId") String contactId,
@Valid @ModelAttribute ContactForm contactForm,BindingResult result, Model model){


     if(result.hasErrors()) {
       model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact";
     }



     var contact =  contactService.getById(contactId);
     contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setFavorite(contactForm.isFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setWebsiteLink(contactForm.getWebsite());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
       

        // Process image


        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }else{
            log.info("No image uploaded!, Please upload an image");
        }
      
       var updateContact =  contactService.updateContact(contact);
         log.info("Updated contact : {}", updateContact);

         model.addAttribute("message", Message.builder()
         .content(updateContact.getName() + " has been updated").type(MessageType.green).build());



 
    return "redirect:/user/contacts/edit_contact/"+contactId;

}






}
