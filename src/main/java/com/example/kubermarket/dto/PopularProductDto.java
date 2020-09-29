package com.example.kubermarket.dto;


import lombok.Getter;

@Getter
public class PopularProductDto {

    private Long productId;

    private Long totalCount;

    public PopularProductDto(Long productId, Long totalCount){ //Popular
        this.productId=productId;
        this.totalCount=totalCount;
    }
}
