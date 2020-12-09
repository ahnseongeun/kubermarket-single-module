package com.example.kubermarket.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category,Long> {

    Optional<Category> findByName(String name);

}
