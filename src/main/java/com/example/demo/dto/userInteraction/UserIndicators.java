package com.example.demo.dto.userInteraction;

import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class UserIndicators {

    public int user_visit_id;
    public Integer timeSpent;
    public Duration timeInterval;

    public UserIndicators(Integer timeSpent, Duration timeInterval) {
        this.user_visit_id = 0;
        this.timeSpent = timeSpent;
        this.timeInterval = timeInterval;
    }
}
