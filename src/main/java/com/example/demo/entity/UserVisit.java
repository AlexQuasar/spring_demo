package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(schema = "public", name = "user_visit")
@Getter
@Setter

// TODO: 1/7/20 не совсем так, визит пользователя это скорее то где и сколько он провел времени.
//  Для того чтобы выдавать отчет о среднем времени на сайте за день следует завести другой класс UserDailyAveragePresenceReport, например.
// тогда, мне кажется, еще один класс entity будет лишним и хранить среднее число в принципе нет необходимости, а если оно понадобится,
// то считать его динамически из этой таблицы

public class UserVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "day")
    private LocalDate day;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "url")
    private String url;

    @Column(name = "timeSpent")
    private Integer timeSpent = 0;
}
