package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "mail")
@Getter
@Setter
public class Mail {

    // TODO: 1/26/20 почему mail. У тебя сервис по учету времени пребывания на сайтах. У него должен быть барьер
    //  чтобы предоставлять данные избирательно. Соответственно нужен список администраторов у которых есть доступ. Список поплняемый.


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
}
