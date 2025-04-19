package com.scm.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component("timeUtil")
public class TimeAgoUtil {
    public String timeAgo(LocalDateTime pastTime) {
        Duration duration = Duration.between(pastTime, LocalDateTime.now());

        if (duration.toMinutes() < 1) return "Just now";
        if (duration.toMinutes() < 60) return duration.toMinutes() + " mins ago";
        if (duration.toHours() < 24) return duration.toHours() + " hrs ago";
        if (duration.toDays() < 7) return duration.toDays() + " days ago";

        return pastTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
}
