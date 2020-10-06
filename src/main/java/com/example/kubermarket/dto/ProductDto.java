package com.example.kubermarket.dto;
import com.example.kubermarket.domain.ProductImage;
import com.example.kubermarket.domain.ProductReview;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ProductDto {

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
    private Long userId;
    private List<ProductImage> productImages;
    private ProductReview productReview;

}
