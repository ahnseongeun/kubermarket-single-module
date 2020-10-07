package com.example.kubermarket.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom,Long> {
    void deleteById(Long sellerId);
}
