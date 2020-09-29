package com.example.kubermarket.service;

public class PasswordWrongException extends RuntimeException {
    PasswordWrongException(){
        super("password is wrong");
    }
}
