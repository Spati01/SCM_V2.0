package com.scm.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.Entitity.User;
import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Services.UserService;
import com.scm.forms.UserForm;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {


  @Autowired 
  private UserService uService;


@GetMapping("/")
public String homeOpen() {
    
    return "redirect:/home";
}



    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("name", "Smart contact managment");
        model.addAttribute("study", "Computer Science");
        
        System.out.println("Home page Handeler");

        return "home";
    }

//about route
@GetMapping("/about")
public String about() {

    System.out.println("This is about page ");
    return "about";
}




//Services route
@GetMapping("/services")
public String service() {

    System.out.println("This is Services page ");
    return "services";
}

//Contact route
@GetMapping("/contact")
public String Contact() {
    return "contact";
}

//Login page
@GetMapping("/login")
public String Login() {
    return "login";
}





// Registration page 

@GetMapping("/register")
public String register(Model model) {
     
    UserForm userForm = new UserForm();
   
    model.addAttribute("userForm", userForm);
    
    return "register";
}


// Registration page 
@PostMapping("/do-register")
public String Register(@Valid  @ModelAttribute UserForm userForm,BindingResult result, HttpSession session) {
 //fatch data

System.out.println("UserForm : "+userForm);

//User form



//validate form data
if(result.hasErrors()){
    return "register";
}

//save to database
//User Service Use
// UserForm --> User 
/* 
User user = User.builder()
.name(userForm.getName())
.email(userForm.getEmail())
.password(userForm.getPassword())
.about(userForm.getAbout())
.phoneNumber(userForm.getPhoneNumber())
.build();

*/
    User user = new User();
user.setName(userForm.getName());
user.setEmail(userForm.getEmail());
user.setPassword(userForm.getPassword());
user.setPhoneNumber(userForm.getPhoneNumber());
user.setAbout(userForm.getAbout());
user.setEnabled(false);
user.setProfilePic("/images/user.png"); 





User saveUser = uService.saveUser(user);

System.out.println("User Save :"+ saveUser);
//message ="Registration Successfully"
 Message message = Message.builder().content("Registration Successfully").type(MessageType.green).build();
session.setAttribute("message", message);

//redirect 
    return "redirect:/register";
}



}
