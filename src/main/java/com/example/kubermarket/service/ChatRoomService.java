package com.example.kubermarket.service;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.domain.ChatRoom;
import com.example.kubermarket.domain.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> getChatRooms(){
        List<ChatRoom> chatRooms= (List<ChatRoom>) chatRoomRepository.findAll();
        return chatRooms;
    }
}
