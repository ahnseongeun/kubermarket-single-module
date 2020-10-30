package com.example.kubermarket.controller;

public class EmailNotExistedException extends RuntimeException{
    public EmailNotExistedException(String email){
        super("Email is not Registered" + email);
    }
}
