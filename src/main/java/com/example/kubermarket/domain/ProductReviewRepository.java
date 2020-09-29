package com.example.kubermarket.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductReviewRepository extends CrudRepository<ProductReview,Long> {
    List<ProductReview> findAllById(Long productId);
}
