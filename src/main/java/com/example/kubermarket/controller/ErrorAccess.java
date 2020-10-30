package com.example.kubermarket.controller;

public class ErrorAccess extends RuntimeException{
    public ErrorAccess(){super("잘못된 접근입니다.");}
}
