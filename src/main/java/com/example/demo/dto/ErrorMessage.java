package com.example.demo.dto;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    ;

    private int index;
    private String message;

    ErrorMessage(int index, String message) {
        this.index = index;
        this.message = message;
    }
}
