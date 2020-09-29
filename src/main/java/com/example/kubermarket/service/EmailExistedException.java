package com.example.kubermarket.service;

public class EmailExistedException extends Throwable {
    public EmailExistedException(String email) {
        super("해당 이메일이 이미 존재합니다.");
    }
}
