package com.scm.Services.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.Entitity.ActivityLog;
import com.scm.Entitity.Contact;
import com.scm.Entitity.User;
import com.scm.Repository.ContactRepo;
import com.scm.Services.ActivityLogService;
import com.scm.Services.ContactService;
import com.scm.Services.ImageService;
import com.scm.Services.UserService;
import com.scm.config.AuthenticatedUserProvider;
import com.scm.forms.ContactForm;
import com.scm.forms.DayData;

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

    @Autowired
    private ActivityLogService activityLogService;
    
  

    @Override
    public Contact saveContact(ContactForm contact) {

        //  Get Logged-in User Directly
        User user = userProvider.getLoggedInUser();

        
        Contact contact_data = new Contact();
        contact_data.setId(UUID.randomUUID().toString());
        contact_data.setName(contact.getName());
        contact_data.setEmail(contact.getEmail());
        contact_data.setPhoneNumber(contact.getPhoneNumber());
        contact_data.setAddress(contact.getAddress());
        contact_data.setWebsiteLink(contact.getWebsite());
        contact_data.setDescription(contact.getDescription());
        contact_data.setLinkedInLink(contact.getLinkedInLink());

       
        contact_data.setCreatedAt(LocalDateTime.now());
        

       if(contact.getContactImage() != null && !contact.getContactImage().isEmpty()){
         //  Generate Image Name
         String filename = UUID.randomUUID().toString();
         // Upload Image to Cloudinary
         String file_url = imageService.uploadImage(contact.getContactImage(), filename);
         log.info("This is image name : {}", file_url);
         contact_data.setPicture(file_url);
         contact_data.setCloudinaryImagePublicId(filename);
       }




        contact_data.setFavorite(contact.isFavorite());
        contact_data.setUser(user);

        Contact savedContact = repo.save(contact_data);
    
    // Log activity
    activityLogService.logActivity(user, savedContact.getName(), ActivityLog.ActionType.CREATED);
    
    return savedContact;
    }

    @Override
    public Contact updateContact(Contact contact) {

        var  oldContact = getById(contact.getId());

        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setPhoneNumber(contact.getPhoneNumber());
        oldContact.setAddress(contact.getAddress());
        oldContact.setWebsiteLink(contact.getWebsiteLink());
        oldContact.setDescription(contact.getDescription());
        oldContact.setLinkedInLink(contact.getLinkedInLink());
        oldContact.setFavorite(contact.isFavorite());
        oldContact.setPicture(contact.getPicture());
        oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        
      
        Contact updatedContact = repo.save(oldContact);
    
        // Log activity
        activityLogService.logActivity(updatedContact.getUser(), 
                                     updatedContact.getName(), 
                                     ActivityLog.ActionType.UPDATED);
        
        return updatedContact;
     
       
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

       // Log activity
    activityLogService.logActivity(contact.getUser(), 
    contact.getName(), 
    ActivityLog.ActionType.DELETED);
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

    @Override
    public ContactForm updateContactForm(String id) {
       
         Contact contact = getById(id);
         
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setWebsite(contact.getWebsiteLink());
        contactForm.setDescription(contact.getDescription());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setFavorite(contact.isFavorite());
       contactForm.setPicture(contact.getPicture());
        return contactForm;

    }



    @Override
    public long getContactCountForUser(String email) {
        return repo.countByUserEmail(email);
    }

    @Override
    public Map<String, DayData> getWeeklyAnalytics(String email) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(6).with(LocalTime.MIN);
        LocalDateTime end = now.with(LocalTime.MAX);
        
        List<Contact> contacts = repo.findContactsByUserEmailAndCreatedAtBetween(email, start, end);
    
        // Initialize daily data for last 7 days
        Map<LocalDate, DayData> dailyData = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = now.minusDays(i).toLocalDate();
            dailyData.put(date, new DayData(0, 0.0, 0.0));
        }
    
        // Populate contact counts
        contacts.forEach(contact -> {
            LocalDate date = contact.getCreatedAt().toLocalDate();
            dailyData.computeIfPresent(date, (key, data) -> {
                data.setCount(data.getCount() + 1);
                return data;
            });
        });
    
        Map<String, DayData> result = new LinkedHashMap<>();
        double[] accumulatedTrend = {0};
    
        // Process days in chronological order
        List<LocalDate> sortedDates = dailyData.keySet().stream()
                .sorted()
                .toList();
    
                for (LocalDate date : sortedDates) {
                    DayData data = dailyData.get(date);
                
                    // Calculate daily trend (not accumulated)
                    double dailyContribution = data.getCount() * 5.0;
                    double trend = Math.min(Math.max(dailyContribution, 0.0), 100.0);
                
                    data.setTrendPercentage(trend);
                    data.setChange(dailyContribution);
                
                    String dayName = date.getDayOfWeek()
                        .getDisplayName(TextStyle.SHORT, Locale.US)
                        .toUpperCase();
                
                    result.put(dayName, data);
                }           
    
        return result;
    }

    @Override
    public double getWeekChange(String email) {
        // Get contact data for current and previous week
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfLastWeek = now.minusDays(7);
        LocalDateTime startOfLastWeek = endOfLastWeek.minusDays(7);
        
        List<Contact> lastWeekContacts = repo.findContactsByUserEmailAndCreatedAtBetween(
            email, startOfLastWeek, endOfLastWeek
        );
        List<Contact> currentWeekContacts = repo.findContactsByUserEmailAndCreatedAtBetween(
            email, endOfLastWeek, now
        );
    
        double lastWeekCount = lastWeekContacts.size();
        double currentWeekCount = currentWeekContacts.size();
    
        // Handle edge cases
        if (lastWeekCount == 0 && currentWeekCount == 0) {
            return 0.0; // No contacts in either week
        }
        if (lastWeekCount == 0) {
            // When no previous data, show actual growth percentage
            return currentWeekCount * 100.0;
        }
    
        // Calculate actual percentage change
        return ((currentWeekCount - lastWeekCount) / lastWeekCount) * 100;
    }
    

    @Override
    public List<Contact> getAllContactsForUser(String email) {
    return repo.findByUserEmailOrderByCreatedAtDesc(email);
}



@Override
public long getFavoriteContactCountForUser(String email) {
    return repo.countByUserEmailAndFavoriteTrue(email); // Count only the favorite contacts for the specific user
}

@Override
public long getUnFavoriteContactCountForUser(String email){
    return repo.countByUserEmailAndFavoriteFalse(email); 
}






}