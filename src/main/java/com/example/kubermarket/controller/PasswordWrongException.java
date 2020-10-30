package com.example.kubermarket.controller;

public class PasswordWrongException extends RuntimeException {
    public PasswordWrongException(){
        super("password is wrong");
    }
}
