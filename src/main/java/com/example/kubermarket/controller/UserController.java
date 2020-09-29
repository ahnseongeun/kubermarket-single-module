package com.example.kubermarket.controller;

import com.example.kubermarket.domain.User;
import com.example.kubermarket.service.EmailExistedException;
import com.example.kubermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody // 관리자가 user 관리할때
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> AllUserList() {
        List<User> userList = userService.getUsers();
        return userList;
    }

    @ResponseBody
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public User create(
            @Valid
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address1") String address1,
            @RequestParam(value = "address2",required = false) String address2,
            @RequestParam("nickname") String nickName,
            @RequestParam(value = "profileImage",required = false) MultipartFile profileImage
    ) throws URISyntaxException, IOException, EmailExistedException {
        LocalDateTime createDate= LocalDateTime.now();
        return userService.AddUser(email,password,address1,address2,nickName,profileImage,createDate);
    }

}