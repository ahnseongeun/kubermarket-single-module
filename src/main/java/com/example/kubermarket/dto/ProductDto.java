package com.example.kubermarket.dto;
import com.example.kubermarket.domain.ProductImage;
import com.example.kubermarket.domain.ProductReview;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.key.ZoneIdKeyDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private Long id;
    private String title;
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateDate;
    private Integer price;
    private Integer interestCount;
    private String status;
    private String address;
    private String categoryName;
    private String nickName;
    private Long userId;
    private List<ProductImage> productImages;
    private ProductReview productReview;

//    @JsonCreator
//    @Builder
//    public ProductDto(
//            @JsonProperty("id") Long id,
//            @JsonProperty("title") String title,
//            @JsonProperty("content") String content,
//            @JsonProperty("createDate") LocalDateTime createDate,
//            @JsonProperty("updateDate") LocalDateTime updateDate,
//            @JsonProperty("price") Integer price,
//            @JsonProperty("interestCount") Integer interestCount,
//            @JsonProperty("status") String status,
//            @JsonProperty("address") String address,
//            @JsonProperty("categoryName") String categoryName,
//            @JsonProperty("userId") Long userId,
//            @JsonProperty("productImages") List<ProductImage> productImages,
//            @JsonProperty("productReview") ProductReview productReview
//    ){
//        this.id=id;
//        this.title=title;
//        this.content=content;
//        this.createDate=createDate;
//        this.updateDate=updateDate;
//        this.price=price;
//        this.interestCount=interestCount;
//        this.status=status;
//        this.address=address;
//        this.categoryName=categoryName;
//        this.userId=userId;
//        this.productImages=productImages;
//        this.productReview=productReview;
//    }

}
