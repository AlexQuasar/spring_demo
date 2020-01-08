package com.example.demo.dto.userInteraction;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@AllArgsConstructor
@EqualsAndHashCode
public class UserSite {

    public int user_id;
    public LocalDate day;
    public String userId;
    public String url;
}
