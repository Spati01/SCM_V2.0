package com.scm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.Entitity.Contact;
import com.scm.Helper.AppConstants;
import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Services.ContactService;
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
        Model model) {

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
        @RequestParam(value="size" , defaultValue= AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value="page", defaultValue= "0") int page,
        @RequestParam(value="sortBy", defaultValue= "name") String sortBy,
        @RequestParam(value="direction", defaultValue= "asc") String direction,
        Model model
        ){

        log.info("Searching contacts for : {} - {}", contactSearchForm.getField(), contactSearchForm.getValue()); 


           Page<Contact> pageContact = null;

        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact =  contactService.searchByName(contactSearchForm.getValue(), page, size, sortBy, direction);
        
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact =  contactService.searchByEmail(contactSearchForm.getValue(), page, size, sortBy, direction);
        }
        
        else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact =  contactService.searchByPhoneNumber(contactSearchForm.getValue(), page, size, sortBy, direction);
        }

        log.info("pageContact : {}",contactSearchForm.getField(),contactSearchForm.getValue());
       model.addAttribute("contactSearchForm",contactSearchForm);
       
       model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("pageContacts", pageContact);

        

        return "user/search_contacts";
    }

}
