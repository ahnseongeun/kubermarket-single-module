package com.example.kubermarket.controller;

import com.example.kubermarket.domain.User;
import com.example.kubermarket.service.EmailExistedException;
import com.example.kubermarket.service.ErrorAccess;
import com.example.kubermarket.service.UserService;
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
    @ApiOperation(value = "ALL UserList (admin)", notes = "모든 사용자 조회")
    public List<User> AllUserList() {
        List<User> userList = userService.getUsers();
        return userList;
    }

    @ResponseBody // 단일 user 정보 불러올때
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ApiOperation(value = "User Detail (Client)", notes = "사용자 세부정보 조회")
    public User getUser(
                        //@Valid @PathVariable Long id,
                        Authentication authentication) {
        if(authentication==null){
            throw new ErrorAccess();
        }
        Claims claims= (Claims) authentication.getPrincipal();
        String nickName = claims.get("nickName", String.class);
        User user = userService.getUser(nickName);
        return user;
    }

    @ResponseBody
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @ApiOperation(value = "User ADD (Client)", notes = "사용자 추가")
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

    @ResponseBody // user 정보 수정할때
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "User Update (Client)", notes = "사용자 정보 수정")
    public User updateUser(
            @Valid
            @PathVariable Long id,
            @RequestParam("password") String password,
            @RequestParam("address1") String address1,
            @RequestParam(value = "address2",required = false) String address2,
            @RequestParam(value = "profileImage",required = false) MultipartFile profileImage
            , Authentication authentication
    ) throws URISyntaxException, IOException, EmailExistedException {
        if(authentication==null){
            throw new ErrorAccess();
        }
        Claims claims= (Claims) authentication.getPrincipal();
        String nickName = claims.get("nickName", String.class);
        return userService.updateUser(id,password,address1,address2,nickName,profileImage);
    }

    @ResponseBody // user 정보 수정할때
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "User Delete (Client)", notes = "사용자 삭제")
    public String deleteUser(
            @Valid
            @PathVariable Long id
            ,Authentication authentication
    ) throws URISyntaxException, IOException, EmailExistedException {
        if(authentication==null){
            throw new ErrorAccess();
        }
        Claims claims= (Claims) authentication.getPrincipal();
        String nickName = claims.get("nickName", String.class);
        userService.deleteUser(id);
        return "{삭제완료}";
    }
}
