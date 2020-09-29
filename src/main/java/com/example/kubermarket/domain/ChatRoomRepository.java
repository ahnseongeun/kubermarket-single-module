package com.example.kubermarket.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRoomRepository extends CrudRepository<ChatRoom,Long> {
    List<ChatRoom> findAllByProductOrderById(Product product);
}
