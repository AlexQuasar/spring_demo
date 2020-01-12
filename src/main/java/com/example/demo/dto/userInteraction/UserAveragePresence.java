package com.example.demo.dto.userInteraction;

import com.example.demo.entity.UserVisit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAveragePresence {

    UserVisit userVisit;
    Integer average;
}
