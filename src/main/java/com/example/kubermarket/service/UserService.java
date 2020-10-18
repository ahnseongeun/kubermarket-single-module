package com.example.kubermarket.service;

import com.example.kubermarket.domain.*;
import com.sun.xml.bind.v2.runtime.output.Encoded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    String fileUrl =  System.getProperty("user.home") + "\\files";

    public List<User> getUsers() {
        List<User> userList= (List<User>) userRepository.findAll();
        return userList;
    }

    @Cacheable(key = "#nickname",value = "user",cacheManager = "CacheManager")
    public User getUser(String nickname) {
        User user= userRepository.findByNickName(nickname);
        String imageUrl= user.getProfileImageUrl();
        List<Product> products = new ArrayList<>();
        for(Product product: user.getProducts()){
            List<ProductImage> productImages=new ArrayList<>();
            for(ProductImage productImage:product.getProductImages()) {
                productImages.add(productImage);
            }
            product.setProductImages(productImages);
            products.add(product);
        }
        List<ProductReview> productReviews = new ArrayList<>();
        for(ProductReview productReview: user.getProductReviews()){
            productReviews.add(productReview);
        }
        List<ChatRoom> chatRooms = new ArrayList<>();
        for(ChatRoom chatRoom: user.getChatRooms()){
            chatRooms.add(chatRoom);
        }
        User newUser= User.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .address1(user.getAddress1())
                .address2(user.getAddress2())
                .email(user.getEmail())
                .password(user.getPassword())
                .createDate(user.getCreateDate())
                .products(products)
                .profileImageUrl(imageUrl)
                .chatRooms(chatRooms)
                .productReviews(productReviews)
                .build();

        user.setProducts(products);
        return newUser;
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public void deleteUser(Long id) {
        //Product product = productRepository.findById(id).orElse(null);
        //chatRoomRepository.deleteById(product.getUser().getId());
        userRepository.deleteById(id);
    }
}
