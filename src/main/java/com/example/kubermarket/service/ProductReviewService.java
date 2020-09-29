package com.example.kubermarket.service;

import com.example.kubermarket.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductReview addProductReview(Long productId, String content, String nickName, LocalDateTime createDate) {
        User user= userRepository.findByNickName(nickName);
        Product product = productRepository.findById(productId).orElse(null);
        ProductReview productReview= ProductReview.builder()
                .content(content)
                .product(product)
                .user(user)
                .nickName(nickName)
                .createDate(createDate)
                .build();
        return productReviewRepository.save(productReview);
    }

    public ProductReview updateProductReview(Long id, String content, LocalDateTime updateDate) {
        ProductReview productReview= productReviewRepository.findById(id).orElse(null);
        productReview.setContent(content);
        productReview.setUpdateDate(updateDate);
        return productReviewRepository.save(productReview);
    }

    public void DeleteProductReview(Long id) {
        productReviewRepository.deleteById(id);
    }
}
