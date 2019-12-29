package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(schema = "public", name = "user_visit_time")
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate day;

    @Column(name = "userId", unique = true)
    private String userId;

    @Column(name = "utl")
    private String url;

    @Column(name = "average")
    private Long average = 0L;
}
