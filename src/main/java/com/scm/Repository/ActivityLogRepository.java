package com.scm.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.Entitity.ActivityLog;
import com.scm.Entitity.User;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByUserOrderByTimestampDesc(User user);

    List<ActivityLog> findByUserOrderByTimestampDesc(User user);


}