package com.example.kubermarket.service;

import com.example.kubermarket.domain.*;
import com.example.kubermarket.dto.PopularProductDto;
import com.example.kubermarket.dto.ProductDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RedisTemplate<String, Object> redisTemplate;
    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;
    public final CategoryRepository categoryRepository;
    public final UserRepository userRepository;

    @Cacheable(key = "#id",value = "Products",cacheManager = "CacheManager")
    public List<Product> getProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return  products;
    }

    //@Cacheable(key = "#id",value = "PopularProduct",cacheManager = "CacheManager")
    public List<PopularProductDto> getPopularProducts(Integer pageNum) {
        Pageable pageRequest = PageRequest.of(0,5*pageNum);
        Page<Product> productList = productRepository.findByPopular(pageRequest);
        List<PopularProductDto> productDtoList= new ArrayList<>();
        Integer count=0;
        for(Product product: productList){
            //ProductId와 채팅수 가져오기
            productDtoList.add(this.convertEntityToDto(product,count+=10));
        }
        //productDtoList.sort((o1, o2) -> ((o2.getInterestCount()+o2.getChatCount()) - (o1.getInterestCount()+o1.getChatCount())));

        return  productDtoList;
//        List<Object> objects= productRepository.findByInterestCountANDChatRoom();
//        List<ProductDto> productDtoList= new ArrayList<>();
//        for(Object object: objects){
//            Object[] result= (Object[]) object;
//            Product product= (Product) result[0];
//            log.info(String.valueOf(Integer.parseInt(String.valueOf(result[1]))));
//            productDtoList.add(this.convertEntityToDto(product));
//        }
    }

    @Cacheable(key = "#address.concat(#pageNum)",value = "AddressProduct",cacheManager = "CacheManager")
    public  List<ProductDto> getAddressProducts(String address,Integer pageNum) {
        Pageable pageRequest = PageRequest.of(0,5*pageNum);
        Page<Product> products= productRepository.findByAddress(address,pageRequest);
        List<ProductDto> productDtoList= new ArrayList<>();
        for(Product product: products){
            productDtoList.add(this.convertEntityToDto(product));
        }
        return  productDtoList;
    }

    @Cacheable(key = "#keyword.concat(#pageNum)",value = "KeywordProduct",cacheManager = "CacheManager")
    public List<ProductDto> getKeywordProducts(String keyword,Integer pageNum) {
        //JPA에서 limit을 사용하는 대신에 pageRequest를 사용해야 한다.
        Pageable pageRequest = PageRequest.of(0,5*pageNum);
        Page<Product> products= productRepository.findByKeyword('%'+keyword+'%',pageRequest);
        List<ProductDto> productDtoList= new ArrayList<>();
        for(Product product: products){
            productDtoList.add(this.convertEntityToDto(product));
        }
        return  productDtoList;
    }

    private ProductDto convertEntityToDto(Product product) {
        ProductDto productDto= new ProductDto();
        return productDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .price(product.getPrice())
                .interestCount(product.getInterestCount())
                .status(product.getStatus())
                .nickName(product.getUser().getNickName())
                .address(product.getUser().getAddress1())
                .categoryName(product.getCategory().getName())
                .build();
    }

    private PopularProductDto convertEntityToDto(Product product,Integer count) {
        return PopularProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .price(product.getPrice())
                .interestCount(product.getInterestCount())
                .status(product.getStatus())
                .address(product.getUser().getAddress1())
                .chatCount(count)
                .nickName(product.getUser().getNickName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    String fileUrl =  System.getProperty("user.home") + "\\files";
    @Transactional
    public Product addProduct(String title, String content, LocalDateTime createDate, LocalDateTime updateDate,
                              Integer price, Integer interestCount, String status, String categoryName, String nickName, List<MultipartFile> files) throws IOException {

        Category category = categoryRepository.findByName(categoryName).orElse(null);
        User user = userRepository.findByNickName(nickName);
        log.info(categoryName);
        log.info(String.valueOf(category));
        Product product= Product.builder()
                .title(title)
                .content(content)
                .createDate(createDate)
                .updateDate(updateDate)
                .price(price)
                .interestCount(interestCount)
                .status(status)
                .category(category)
                .user(user)
                .build();
        productRepository.save(product);

        List<ProductImage> productImageList = new ArrayList<>();
        if(files!=null) {
            for (MultipartFile file : files) {
                log.info(file.getOriginalFilename());
                log.info(file.getContentType());
                String filename = file.getOriginalFilename();
                String filePath = fileUrl + "\\" + filename;
                file.transferTo(new File(filePath));
                ProductImage productImage = ProductImage.builder()
                        .fileUrl(filePath)
                        .filename(filename)
                        .deleteFlag(true)
                        .product(product)
                        .build();
                productImageList.add(productImageRepository.save(productImage));
            }
        }
        return product;
    }


    @Cacheable(key = "#id",value = "DetailProduct",cacheManager = "CacheManager")
    public ProductDto getDetailProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        //캐시를 적용하면 트랜잭션때문에 얕은 복사를 할 경우에 DB에서 참조된 값을 가져오지 못하기 때문에
        //"LazyInitializationException: could not initialize proxy - no Session" 에러가 난다.
        // 그래서 리스트를 새로만들어 하나하나 넣어주는 깊은 복사를 해서 처리 해줘야 한다.
        //JPA에서 Lazy Evaluation은 값은 지금당장 필요없고, 레퍼런스만 리턴해도 될 때 사용합니다.
        //Lazy Evaluation의 결과 나중에 값을 가져올 수 있는 프록시 객체가 리턴됩니다.
        //프록시 객체는 영속성 컨텍스트 안에서만 동작합니다.
        List<ProductImage> productImageList = new ArrayList<>();
        for(ProductImage productImage: product.getProductImages()){
            productImageList.add(productImage);
        }
        //log.info(String.valueOf(product));
        log.info("pass");
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .price(product.getPrice())
                .interestCount(product.getInterestCount())
                .status(product.getStatus())
                .address(product.getUser().getAddress1())
                .nickName(product.getUser().getNickName())
                .categoryName(product.getCategory().getName())
                .userId(product.getUser().getId())
                .productImages(productImageList)
                .productReview(product.getProductReview())
                .build();
        //log.info(String.valueOf(productDto));
        log.info("pass2");
        return productDto;
        //TODO
    }

    @Transactional
    public Product updateProduct(Long id,String title, String content, LocalDateTime updateDate,
                                 Integer price, Integer interestCount, String status,String categoryName,String nickName,
                                List<MultipartFile> files) throws IOException {
        Product product= productRepository.findById(id).orElse(null);
        product.updateProduct(title,content,updateDate, price,interestCount,status);
        product.setCategory(categoryRepository.findByName(categoryName).orElse(null));
        for(MultipartFile file :files) {
            log.info(file.getContentType());
            String filename = file.getOriginalFilename();
            String filePath = fileUrl + "\\" + filename;
            file.transferTo(new File(filePath));
            ProductImage productImage = ProductImage.builder()
                    .fileUrl(filePath)
                    .filename(filename)
                    .deleteFlag(true)
                    .product(product)
                    .build();
            productImageRepository.save(productImage);
        }
        productRepository.save(product);
        return product;
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage productImage= productImageRepository.findById(imageId).orElse(null);
        //productImage.setDeleteFlag(false);
        //productImageRepository.save(productImage);
        productImageRepository.deleteById(imageId);

    }


}
