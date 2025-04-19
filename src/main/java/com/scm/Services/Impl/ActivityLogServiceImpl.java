package com.scm.Services.Impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.Entitity.ActivityLog;

import com.scm.Entitity.User;
import com.scm.Repository.ActivityLogRepository;
import com.scm.Services.ActivityLogService;
@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepo;

    @Override
    public void logActivity(User user, String contactName, ActivityLog.ActionType actionType) {
        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setContactName(contactName);
        log.setActionType(actionType);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepo.save(log);
    }

    @Override
    public Map<ActivityLog.ActionType, ActivityLog> getLatestActivities(User user) {
        List<ActivityLog> allLogs = activityLogRepo.findTop10ByUserOrderByTimestampDesc(user); // get more if needed
        Map<ActivityLog.ActionType, ActivityLog> latest = new EnumMap<>(ActivityLog.ActionType.class);
    
        for (ActivityLog.ActionType type : ActivityLog.ActionType.values()) {
            latest.put(type, null); // initialize all slots
        }
    
        for (ActivityLog log : allLogs) {
            if (latest.get(log.getActionType()) == null) {
                latest.put(log.getActionType(), log);
            }
        }
    
        return latest;
    }


    @Override
    public List<ActivityLog> getAllActivitiesSorted(User user) {
        return activityLogRepo.findByUserOrderByTimestampDesc(user);
    }
    


}
