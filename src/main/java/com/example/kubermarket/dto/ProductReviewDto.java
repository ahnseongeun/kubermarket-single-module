package com.example.kubermarket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ProductReviewDto {
    private String content;
    private LocalDateTime createDate;

}
