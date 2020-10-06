package com.example.kubermarket.service;

public class ErrorAccess extends RuntimeException{
    public ErrorAccess(){super("잘못된 접근입니다.");}
}
