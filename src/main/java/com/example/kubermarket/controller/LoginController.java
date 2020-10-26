package com.example.kubermarket.controller;

import com.example.kubermarket.JwtUtil;
import com.example.kubermarket.domain.User;
import com.example.kubermarket.dto.LoginRequestDto;
import com.example.kubermarket.dto.LoginResponseDto;
import com.example.kubermarket.service.ErrorAccess;
import com.example.kubermarket.service.LoginService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping(value = "/api")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final LoginService loginService;

    @Autowired
    public LoginController(JwtUtil jwtUtil, LoginService loginService) {
        this.jwtUtil = jwtUtil;
        this.loginService = loginService;
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "Login (client)", notes = "로그인을 통해서 token 얻기")
    public LoginResponseDto create(
            @RequestBody LoginRequestDto requestDto){
        User user= loginService.authenticate(requestDto.getEmail(),requestDto.getPassword());
        String accessToken = jwtUtil.creatToken(user.getEmail(),user.getNickName());
        LoginResponseDto ReceiveToken = LoginResponseDto.builder().accessToken(accessToken).build();
        log.info(ReceiveToken.getAccessToken());
        return ReceiveToken;
    }

//    @ResponseBody
//    @RequestMapping(value = "/logout",method = RequestMethod.DELETE)
//    public String delete(
//            Authentication authentication){
//        if(authentication==null){
//            throw new ErrorAccess();
//        }
//        Claims claims= (Claims) authentication.getPrincipal();
//        String nickName = claims.get("nickName", String.class);
//        userService.deleteUser(id);
//        return "logout 되었습니다.";
//    }


}
