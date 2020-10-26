package com.example.kubermarket.controller;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.domain.ChatRoom;
import com.example.kubermarket.service.CategoryService;
import com.example.kubermarket.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping(value = "/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService=chatRoomService;
    }

    @ResponseBody
    @RequestMapping(value = "/chatrooms", method = RequestMethod.GET)
    @ApiOperation(value = "ChatRoom List(관리자 기능)", notes = "채팅방 목록 불러오기")
    public List<ChatRoom> list(){
        List<ChatRoom> chatRooms = chatRoomService.getChatRooms();

        return chatRooms;
    }


}
