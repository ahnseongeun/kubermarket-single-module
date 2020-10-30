package com.example.kubermarket.controller;

import com.example.kubermarket.domain.ProductReview;
import com.example.kubermarket.dto.ProductReviewDto;
import com.example.kubermarket.service.ProductReviewService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @ApiOperation(value = "ProductReview ADD (Client)", notes = "productReview 추가")
    public ProductReview AddProductReview(
            @Valid
            @PathVariable Long productId,
            @RequestParam("content") String content
            ,Authentication authentication
            ) throws URISyntaxException, IOException {
            if(authentication==null){
                throw new ErrorAccess();
            }
            Claims claims= (Claims) authentication.getPrincipal();
            String nickName = claims.get("nickName", String.class);
            //String userId = resource.getUserId();
            LocalDateTime createDate = LocalDateTime.now();
            ProductReview productReview= productReviewService.addProductReview(productId,content,nickName,createDate);
            return productReview;
    }

    @ResponseBody//Product에 해당하는 Review 수정
    @RequestMapping(value = "/product/review/{id}",method = RequestMethod.PATCH)
    @ApiOperation(value = "ProductReview Update (Client)", notes = "productReview 수정")
    public ProductReview updateProductReview(
            @Valid
            @PathVariable Long id,
            @RequestParam("content") String content
            ,Authentication authentication){
        if(authentication==null){
            throw new ErrorAccess();
        }
//        Claims claims= (Claims) authentication.getPrincipal();
//        String nickName = claims.get("nickName", String.class);
        //String content = resource.getContent();
        LocalDateTime updateDate = LocalDateTime.now();
        ProductReview productReview= productReviewService.updateProductReview(id,content,updateDate);
        return productReview;
    }

    @ResponseBody//Product에 해당하는 Review 삭제
    @RequestMapping(value = "/product/review/{id}",method = RequestMethod.DELETE)
    @ApiOperation(value = "ProductReview Delete (Client)", notes = "productReview 삭제")
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
