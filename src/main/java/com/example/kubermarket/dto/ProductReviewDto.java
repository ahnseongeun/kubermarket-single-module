package com.example.kubermarket.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductReviewDto {
    private String userId;
    private String content;
    private LocalDateTime createDate;

}
