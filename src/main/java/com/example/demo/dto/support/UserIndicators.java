package com.example.demo.dto.support;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserIndicators {

    public int user_visit_id;
    public Integer timeSpent;

    public UserIndicators(Integer timeSpent) {
        this.user_visit_id = 0;
        this.timeSpent = timeSpent;
    }
}
