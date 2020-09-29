package com.example.kubermarket.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface ProductImageRepository extends CrudRepository<ProductImage,Long> {
}
