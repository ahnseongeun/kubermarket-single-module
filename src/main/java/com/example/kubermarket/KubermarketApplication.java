package com.example.kubermarket;

import com.example.kubermarket.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class KubermarketApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KubermarketApplication.class, args);
    }

	private final ChatMessageRepository chatMessageRepository;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final ProductReviewRepository productReviewRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception{

		// Category Date START
		Category cate1 =new Category();
		cate1.updateInformation("디지털/가전");
		categoryRepository.save(cate1);

		Category cate2 =new Category();
		cate2.updateInformation("유아/유아도서");
		categoryRepository.save(cate2);

		Category cate3 =new Category();
		cate3.updateInformation("스포츠/레저");
		categoryRepository.save(cate3);

		Category cate4 =new Category();
		cate4.updateInformation("여성의류");
		categoryRepository.save(cate4);

		Category cate5 =new Category();
		cate5.updateInformation("게임/취미");
		categoryRepository.save(cate5);

		Category cate6 =new Category();
		cate6.updateInformation("반려동물용품");
		categoryRepository.save(cate6);

		Category cate7 =new Category();
		cate7.updateInformation("기타 중고물품");
		categoryRepository.save(cate7);

		Category cate8 =new Category();
		cate8.updateInformation("가구/인테리어");
		categoryRepository.save(cate8);

		Category cate9 =new Category();
		cate9.updateInformation("생활/가공식품");
		categoryRepository.save(cate9);

		Category cate10 =new Category();
		cate10.updateInformation("여성잡화");
		categoryRepository.save(cate10);

		Category cate11 =new Category();
		cate11.updateInformation("남성패션/잡화");
		categoryRepository.save(cate11);

		Category cate12 =new Category();
		cate12.updateInformation("뷰티/미용");
		categoryRepository.save(cate12);

		Category cate13 =new Category();
		cate13.updateInformation("도서/티켓/음반");
		categoryRepository.save(cate13);
		// Category Data END

		// USER Data START
		User user1 =new User();
		String password1="user1";
		String encodedPassword1= passwordEncoder.encode(password1);
		user1.setEmail("user1@naver.com");
		user1.setNickName("테스트1");
		user1.setPassword(encodedPassword1);
		user1.setAddress1("송파구");
		user1.setAddress2("잠실1동");
		userRepository.save(user1);

		User user2 =new User();
		String password2="user2";
		String encodedPassword2= passwordEncoder.encode(password2);
		user2.setEmail("user2@naver.com");
		user2.setNickName("테스트2");
		user2.setPassword(encodedPassword2);
		user2.setAddress1("송파구");
		user2.setAddress2("잠실2동");
		userRepository.save(user2);

		User user3 =new User();
		String password3="user3";
		String encodedPassword3= passwordEncoder.encode(password3);
		user3.setEmail("user3@naver.com");
		user3.setNickName("테스트3");
		user3.setPassword(encodedPassword3);
		user3.setAddress1("강남구");
		user3.setAddress2("도곡1동");
		userRepository.save(user3);

		User user4 =new User();
		String password4="user4";
		String encodedPassword4= passwordEncoder.encode(password4);
		user4.setEmail("user4@naver.com");
		user4.setNickName("테스트4");
		user4.setPassword(encodedPassword4);
		user4.setAddress1("강남구");
		user4.setAddress2("도곡2동");
		userRepository.save(user4);

		User user5 =new User();
		String password5="user5";
		String encodedPassword5= passwordEncoder.encode(password5);
		user5.setEmail("user5@naver.com");
		user5.setNickName("테스트5");
		user5.setPassword(encodedPassword5);
		user5.setAddress1("관악구");
		user5.setAddress2("신림1동");
		userRepository.save(user5);

		User user6 =new User();
		String password6="user6";
		String encodedPassword6= passwordEncoder.encode(password6);
		user6.setEmail("user6@naver.com");
		user6.setNickName("테스트6");
		user6.setPassword(encodedPassword6);
		user6.setAddress1("관악구");
		user6.setAddress2("신림2동");
		userRepository.save(user6);
		// USER Data END


		// PRODUCT Data START
		Product product= new Product();
		product.setTitle("장미칼");
		product.setPrice(10000);
		product.setContent("해외에서 직접 수입해왔어요");
		product.setCreateDate(LocalDateTime.now());
		product.setInterestCount(0);
		product.setStatus("판매중");
		product.setCategory(cate7);
		product.setUser(user1);
		productRepository.save(product);

		Product product1= new Product();
		product1.setTitle("원피스");
		product1.setPrice(5000);
		product1.setCreateDate(LocalDateTime.now());
		product1.setContent("한번밖에 안입었어요");
		product1.setInterestCount(0);
		product1.setStatus("판매중");
		product1.setCategory(cate4);
		product1.setUser(user2);
		productRepository.save(product1);

		Product product2= new Product();
		product2.setTitle("동화책");
		product2.setPrice(1003);
		product2.setContent("3살용 동화책 묶음집 팔아요");
		product2.setCreateDate(LocalDateTime.now());
		product2.setInterestCount(0);
		product2.setStatus("판매완료");
		product2.setCategory(cate2);
		product2.setUser(user3);
		productRepository.save(product2);

		ProductReview productReview = ProductReview.builder()
				.content("좋았어요")
				.createDate(LocalDateTime.now())
				.user(user4)
				.product(product2)
				.nickName(user4.getNickName())
				.build();
		productReviewRepository.save(productReview);

		for(int i=3;i<50;i++){
			Product product3=new Product();
			product3.setTitle("가전"+i);
			product3.setPrice(i*1000);
			product3.setContent("가전"+i);
			product3.setCreateDate(LocalDateTime.now());
			product3.setInterestCount(i+20);
			product3.setStatus("판매중");
			product3.setCategory(cate1);
			product3.setUser(user4);
			productRepository.save(product3);

			Product product4=new Product();
			product4.setTitle("서핑보드"+i);
			product4.setPrice(i*100000);
			product4.setContent("서핑보드"+i);
			product4.setCreateDate(LocalDateTime.now());
			product4.setInterestCount(i+20);
			product4.setStatus("판매완료");
			product4.setCategory(cate3);
			product4.setUser(user1);
			productRepository.save(product4);

			ProductReview productReview1 = ProductReview.builder()
					.content("좋았어요")
					.createDate(LocalDateTime.now())
					.user(user5)
					.product(product4)
					.nickName(user5.getNickName())
					.build();
			productReviewRepository.save(productReview1);

			Product product5=new Product();
			product5.setTitle("슈퍼마리오"+i);
			product5.setPrice(i*1000);
			product5.setContent("슈퍼마리오"+i);
			product5.setCreateDate(LocalDateTime.now());
			product5.setInterestCount(i+10);
			product5.setStatus("판매중");
			product5.setCategory(cate5);
			product5.setUser(user2);
			productRepository.save(product5);

			Product product6=new Product();
			product6.setTitle("냉동식품"+i);
			product6.setPrice(i*3000);
			product6.setContent("냉동식품"+i);
			product6.setCreateDate(LocalDateTime.now());
			product6.setInterestCount(i+5);
			product6.setStatus("판매중");
			product6.setCategory(cate9);
			product6.setUser(user5);
			productRepository.save(product6);

			Product product7=new Product();
			product7.setTitle("입생로랑 틴트"+i);
			product7.setPrice(i*100);
			product7.setContent("입생로랑 틴트"+i);
			product7.setCreateDate(LocalDateTime.now());
			product7.setInterestCount(i+20);
			product7.setStatus("판매중");
			product7.setCategory(cate12);
			product7.setUser(user6);
			productRepository.save(product7);
		}
		// PRODUCT Data END

		Product product7=new Product();
		product7.setTitle("입생로랑 틴트");
		product7.setPrice(10000);
		product7.setContent("입생로랑 틴트");
		product7.setCreateDate(LocalDateTime.now());
		product7.setInterestCount(70);
		product7.setStatus("판매중");
		product7.setCategory(cate12);
		product7.setUser(user6);
		productRepository.save(product7);

		for(int i=0;i<100;i++){
			ChatRoom chatRoom= ChatRoom.builder()
					.product(product)
					.user(user2)
					.build();
			chatRoomRepository.save(chatRoom);
		}

		for(int i=0;i<50;i++){
			ChatRoom chatRoom= ChatRoom.builder()
					.product(product7)
					.user(user2)
					.build();
			chatRoomRepository.save(chatRoom);
		}

		ChatRoom chatRoom2= ChatRoom.builder()
				.product(product2)
				.user(user1)
				.build();
		chatRoomRepository.save(chatRoom2);

		ChatRoom chatRoom3= ChatRoom.builder()
				.product(product2)
				.user(user6)
				.build();
		chatRoomRepository.save(chatRoom3);

		for(int i=0;i<50;i++){
			ChatMessage chatMessage= ChatMessage.builder()
					.chatRoom(chatRoom3)
					.createDate(LocalDateTime.now())
					.senderType(i%2==1?true:false)
					.message("안녕하세요"+i)
					.build();
			chatMessageRepository.save(chatMessage);
		}

	}

}
