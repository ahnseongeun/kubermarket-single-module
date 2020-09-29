package com.example.kubermarket.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddressProductDto {
    private Long productId;

    private LocalDateTime createDate;

    private String address;

    public AddressProductDto(Long productId, LocalDateTime createDate, String address){ //address
        this.productId=productId;
        this.createDate=createDate;
        this.address=address;
    }
}
