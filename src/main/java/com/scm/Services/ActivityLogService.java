package com.scm.Services;

import java.util.List;
import java.util.Map;

import com.scm.Entitity.ActivityLog;
import com.scm.Entitity.User;

public interface ActivityLogService {


     void logActivity(User user, String contactName, ActivityLog.ActionType actionType);
    public Map<ActivityLog.ActionType, ActivityLog> getLatestActivities(User user);
    List<ActivityLog> getAllActivitiesSorted(User user);
  
}
