package com.example.demo.services.support;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@AllArgsConstructor
@EqualsAndHashCode
public class UserSite {

    public LocalDate day;
    public String userId;
    public String url;
}
