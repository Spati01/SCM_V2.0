package com.scm.controllers;



import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.Entitity.ActivityLog;
import com.scm.Entitity.User;
import com.scm.Helper.Helper;
import com.scm.Repository.ActivityLogRepository;
import com.scm.Services.ActivityLogService;
import com.scm.Services.ContactService;
import com.scm.Services.UserService;
import com.scm.Services.WeatherService;
import com.scm.forms.DayData;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

 
   
  @Autowired
  private UserService service;

  @Autowired
  private ContactService contactService;

  @Autowired
  private ActivityLogService activityLogService;

  @Autowired
  private WeatherService weatherService;
  



    // User dashbaord
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = service.getUserByEmail(email);

        model.addAttribute("LoggedInUser", user);
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "User not authenticated");
            return "error";
        }
   
        // Contact analytics
        Map<String, DayData> weeklyAnalytics = contactService.getWeeklyAnalytics(email);
        model.addAttribute("dayByDayData", weeklyAnalytics);
        
        // Calculate max contacts for trend visualization
        int maxContacts = weeklyAnalytics.values().stream()
                .mapToInt(DayData::getCount)
                .max()
                .orElse(1);
        model.addAttribute("maxContacts", maxContacts);
    
        double weekChange = contactService.getWeekChange(email);
model.addAttribute("weekChange", 
    (weekChange > 0 ? "+" : "") + String.format("%.1f", weekChange));
    
        // Contact data
        model.addAttribute("contactCount", contactService.getContactCountForUser(email));
        model.addAttribute("allContacts", contactService.getAllContactsForUser(email));


         // Get the favorite contact count   
    long favoriteContactCount = contactService.getFavoriteContactCountForUser(email);
    model.addAttribute("favoriteContactCount", favoriteContactCount);

// get the unfavorite contact count

long unfavoriteContactCount = contactService.getUnFavoriteContactCountForUser(email);
model.addAttribute("unfavoriteContactCount", unfavoriteContactCount);




    // 4️⃣ Recent Activity Logs (convert enum keys to string keys for Thymeleaf)
    Map<ActivityLog.ActionType, ActivityLog> enumActivityMap = activityLogService.getLatestActivities(user);
    Map<String, ActivityLog> stringActivityMap = new HashMap<>();

    for (Map.Entry<ActivityLog.ActionType, ActivityLog> entry : enumActivityMap.entrySet()) {
        stringActivityMap.put(entry.getKey().name(), entry.getValue());
    }

    model.addAttribute("latestActivities", stringActivityMap);

 
    List<ActivityLog> activityLog = activityLogService.getAllActivitiesSorted(user);
    model.addAttribute("activityLog", activityLog);



    model.addAttribute("mood", weatherService.getMoodBasedOnTime());
    model.addAttribute("weather", weatherService.getCurrentWeather("Kolkata"));
    model.addAttribute("progress", weatherService.getDailyProgress(user.getUserId()));

        return "user/dashboard";
    }
    
    @GetMapping("/activity-log")
    public String getActivityLog() {
     //   List<ActivityLog> activityLogs = activityLogService.getAllForUser(currentUser());
      //  model.addAttribute("activityLogs", activityLogs);
        return "user/activity-log"; // Thymeleaf template
    }
    
  


 

 // User profile page
 @GetMapping("/profile")
public String userProfile(Authentication authentication, Model model, HttpSession session) {

  // Get logged-in email
  String email = Helper.getEmailOfLoggedInUser(authentication);
  // Fetch user
  User user = service.getUserByEmail(email);

// count contact 
long contactCount = contactService.getContactCountForUser(email);
model.addAttribute("contactCount", contactCount);


  // Get session value
  Boolean showPassword = (Boolean) session.getAttribute("showPassword");
  if (showPassword == null) showPassword = false;

  // Pass to view
  model.addAttribute("LoggedInUser", user);
  model.addAttribute("showPassword", showPassword);

  return "user/profile";
}

@PostMapping("/profile/toggle-password")
public String togglePassword(HttpSession session) {
    Boolean current = (Boolean) session.getAttribute("showPassword");
    session.setAttribute("showPassword", current == null ? true : !current);
    return "redirect:/user/profile";
}





}
