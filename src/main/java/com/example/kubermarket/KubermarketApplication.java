package com.example.kubermarket;

import com.example.kubermarket.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@RequiredArgsConstructor
public class KubermarketApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KubermarketApplication.class, args);
    }


	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final ProductReviewRepository productReviewRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception{

		User user1 =new User();
		String password="123321";
		String encodedPassword= passwordEncoder.encode(password);
		user1.setEmail("ast3138@naver.com");
		user1.setNickName("seongeun");
		user1.setPassword(encodedPassword);
		user1.setAddress1("songpa");
		User user2 =new User();
		user2.setEmail("ast3137@naver.com");
		user2.setNickName("namu");
		user2.setPassword(encodedPassword);
		user2.setAddress1("gangnam");
		userRepository.save(user1);
		userRepository.save(user2);

		Category first =new Category();
		first.setName("Kitchin");
		categoryRepository.save(first);

		Category second =new Category();
		second.setName("Kitchin");
		categoryRepository.save(second);

		Product product= new Product();
		product.setTitle("knife");
		product.setPrice(100);
		product.setContent("advised");
		product.setCreateDate(LocalDateTime.now());
		product.setInterestCount(1);
		product.setStatus("hi");
		product.setCategory(first);
		product.setUser(user1);
		productRepository.save(product);

		ChatRoom chatRoom0 =new ChatRoom();
		chatRoom0.setProduct(product);
		chatRoomRepository.save(chatRoom0);

		Product product1= new Product();
		product1.setTitle("knife1");
		product1.setPrice(1000);
		product1.setCreateDate(LocalDateTime.now());
		product1.setContent("airfares");
		product1.setInterestCount(1);
		product1.setStatus("hi1");
		product1.setCategory(first);
		product1.setUser(user1);
		productRepository.save(product1);

		ChatRoom chatRoom =new ChatRoom();
		chatRoom.setProduct(product1);
		ChatRoom chatRoom1 =new ChatRoom();
		chatRoom1.setProduct(product1);
		ChatRoom chatRoom2 =new ChatRoom();
		chatRoom2.setProduct(product1);
		ChatRoom chatRoom3 =new ChatRoom();
		chatRoom3.setProduct(product1);
		chatRoomRepository.save(chatRoom);
		chatRoomRepository.save(chatRoom1);
		chatRoomRepository.save(chatRoom2);
		chatRoomRepository.save(chatRoom3);

		Product product2= new Product();
		product2.setTitle("knife2");
		product2.setPrice(1003);
		product2.setContent("appeased");
		product2.setCreateDate(LocalDateTime.now());
		product2.setInterestCount(2);
		product2.setStatus("hi2");
		product2.setCategory(first);
		product2.setUser(user2);
		productRepository.save(product2);

		ChatRoom chatRoom4 =new ChatRoom();
		chatRoom4.setProduct(product2);
		ChatRoom chatRoom5 =new ChatRoom();
		chatRoom5.setProduct(product2);
		chatRoomRepository.save(chatRoom4);
		chatRoomRepository.save(chatRoom5);

		Category category = categoryRepository.findById(1L).orElse(null);
		category.updateInfomation("sports");
		categoryRepository.save(category);

		ProductReview productReview = ProductReview.builder()
				.content("good")
				.createDate(LocalDateTime.now())
				.user(user1)
				.product(product1)
				.nickName(user1.getNickName())
				.build();
		productReviewRepository.save(productReview);

		ProductReview productReview1 = ProductReview.builder()
				.content("good")
				.createDate(LocalDateTime.now())
				.user(user2)
				.product(product2)
				.nickName(user2.getNickName())
				.build();
		productReviewRepository.save(productReview1);

	}

}
