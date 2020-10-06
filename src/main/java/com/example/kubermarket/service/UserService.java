package com.example.kubermarket.service;

import com.example.kubermarket.domain.ProductImage;
import com.example.kubermarket.domain.User;
import com.example.kubermarket.domain.UserRepository;
import com.sun.xml.bind.v2.runtime.output.Encoded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    String fileUrl =  System.getProperty("user.home") + "\\files";

    public List<User> getUsers() {
        List<User> userList= (List<User>) userRepository.findAll();
        return userList;
    }

    public User getUser(Long id) {
        User user= userRepository.findById(id).orElse(null);
        return user;
    }

    public User updateUser(Long id,String password,String address1,String address2,
                           String nickName,MultipartFile profileImage) throws IOException {
        User user= userRepository.findById(id).orElse(null);
        String encodedPassword = passwordEncoder.encode(password);
        String filePath = fileUrl + "\\"+ profileImage;
        profileImage.transferTo(new File(filePath));
        user.setPassword(encodedPassword);
        user.setAddress1(address1);
        user.setAddress2(address2);
        user.setNickName(nickName);
        user.setProfileImageUrl(filePath);
        return userRepository.save(user);
    }


    public User AddUser(String email, String password, String address1, String address2,
                                     String nickName, MultipartFile userImage, LocalDateTime createDate) throws IOException, EmailExistedException {

        Optional<User> existed= userRepository.findBynickNameOrEmail(nickName,email);
        if(existed.isPresent()){
            throw new EmailExistedException(email);
        }
        String encodedPassword= passwordEncoder.encode(password);
        String filename = userImage.getOriginalFilename();
        String filePath = fileUrl + "\\" + filename;
        userImage.transferTo(new File(filePath));
        User user= User.builder()
                .email(email)
                .password(encodedPassword)
                .address1(address1)
                .address2(address2)
                .nickName(nickName)
                .profileImageUrl(filePath)
                .createDate(createDate)
                .build();
       return userRepository.save(user);
    }
}
