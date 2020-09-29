package com.example.kubermarket.service;

public class EmailNotExistedException extends RuntimeException{
    EmailNotExistedException(String email){
        super("Email is not Registered" + email);
    }
}
