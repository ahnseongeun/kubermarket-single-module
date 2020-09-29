package com.example.kubermarket.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PopularProductDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer price;
    private Integer interestCount;
    private String status;
    private String address;
    private String categoryName;
    private String nickName;
    private Integer chatCount;
    //private Integer totalCount;
}
