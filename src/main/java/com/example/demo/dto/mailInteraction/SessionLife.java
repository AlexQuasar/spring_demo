package com.example.demo.dto.mailInteraction;

import com.example.demo.entity.Mail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class SessionLife {

    private Mail mail;
    private Date tokenDate;
}
