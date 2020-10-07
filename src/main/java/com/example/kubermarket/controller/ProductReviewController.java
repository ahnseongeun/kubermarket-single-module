package com.example.kubermarket.controller;

import com.example.kubermarket.domain.ProductReview;
import com.example.kubermarket.dto.ProductReviewDto;
import com.example.kubermarket.service.ErrorAccess;
import com.example.kubermarket.service.ProductReviewService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/api")
public class ProductReviewController {

    public final ProductReviewService productReviewService;

    @Autowired
    public ProductReviewController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @ResponseBody//Product에 해당하는 Review 추가
    @RequestMapping(value = "/product/{productId}/review",method = RequestMethod.POST)
    public ProductReview AddProductReview(
            @Valid @RequestBody ProductReviewDto resource,
            @PathVariable Long productId
            ,Authentication authentication
            ) throws URISyntaxException, IOException {
            if(authentication==null){
                throw new ErrorAccess();
            }
//            Claims claims= (Claims) authentication.getPrincipal();
//            String nickName = claims.get("nickName", String.class);
            String content = resource.getContent();
            String userId = resource.getUserId();
            LocalDateTime createDate = LocalDateTime.now();
            ProductReview productReview= productReviewService.addProductReview(productId,content,userId,createDate);
            return productReview;
    }

    @ResponseBody//Product에 해당하는 Review 수정
    @RequestMapping(value = "/product/review/{id}",method = RequestMethod.PATCH)
    public ProductReview updateProductReview(
            @Valid @RequestBody ProductReviewDto resource,
            @PathVariable Long id
            ,Authentication authentication){
        if(authentication==null){
            throw new ErrorAccess();
        }
//        Claims claims= (Claims) authentication.getPrincipal();
//        String nickName = claims.get("nickName", String.class);
        String content = resource.getContent();
        LocalDateTime updateDate = LocalDateTime.now();
        ProductReview productReview= productReviewService.updateProductReview(id,content,updateDate);
        return productReview;
    }

    @ResponseBody//Product에 해당하는 Review 삭제
    @RequestMapping(value = "/product/review/{id}",method = RequestMethod.DELETE)
    public void DeleteProductReview(
            @Valid
            @PathVariable Long id,Authentication authentication){
        if(authentication==null){
            throw new ErrorAccess();
        }
//        Claims claims= (Claims) authentication.getPrincipal();
//        String nickName = claims.get("nickName", String.class);
       productReviewService.DeleteProductReview(id);
    }

}
