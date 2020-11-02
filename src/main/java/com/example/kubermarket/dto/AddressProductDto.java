package com.example.kubermarket.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class AddressProductDto implements Serializable {
    private Long productId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    private String address;

    public AddressProductDto(Long productId, LocalDateTime createDate, String address){ //address
        this.productId=productId;
        this.createDate=createDate;
        this.address=address;
    }
}
