package com.example.demo.dto.userInteraction;

import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public class UserIndicators {

    public int user_visit_id;
    public Integer timeSpent;
    public Duration timeInterval;
    public Integer countAdditions = 1;

    public UserIndicators(Integer timeSpent, Duration timeInterval) {
        this.user_visit_id = 0;
        this.timeSpent = timeSpent;
        this.timeInterval = timeInterval;
    }

    public UserIndicators(int user_visit_id, Integer timeSpent, Duration timeInterval) {
        this.user_visit_id = user_visit_id;
        this.timeSpent = timeSpent;
        this.timeInterval = timeInterval;
    }
}
