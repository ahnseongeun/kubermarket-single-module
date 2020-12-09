package com.example.kubermarket.service;

import com.example.kubermarket.domain.*;
import com.example.kubermarket.dto.PopularProductDto;
import com.example.kubermarket.dto.ProductDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RedisTemplate<String, Object> redisTemplate;
    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;
    public final CategoryRepository categoryRepository;
    public final UserRepository userRepository;
    private String fileUrl =  System.getProperty("user.home") + "\\files";

    public List<Product> getProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return  products;
    }

    //@Cacheable(key = "Popular",value = "PopularProduct",cacheManager = "CacheManager")
    public List<PopularProductDto> getPopularProducts(Integer pageNum) {
        List<PopularProductDto> productDtoList= new ArrayList<>();
        ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
        String Key="PopularProduct::Popular";
        if(redisData.get(Key)==null){ //Cache가 안되었다면 Cache 처리하기
            List<Product> productList = productRepository.findByPopular();
            for(Product product: productList){ //ProductId와 채팅수 가져오기
                productDtoList.add(this.convertPopularEntityToDto(product));
            }
            productDtoList.sort((o1, o2) -> ((o2.getInterestCount()+o2.getChatCount()) - (o1.getInterestCount()+o1.getChatCount())));
            redisData.set(Key,productDtoList);
            redisTemplate.expire(Key,5L, TimeUnit.MINUTES); //5분마다 캐시 갱신
        }else{
            log.info("getPopular_cache");
            productDtoList = (List<PopularProductDto>) redisData.get(Key);
            productDtoList.sort((o1, o2) -> ((o2.getInterestCount()+o2.getChatCount()) - (o1.getInterestCount()+o1.getChatCount())));
            if(pageNum*16>productDtoList.size()) {
                productDtoList = productDtoList.subList(0, productDtoList.size());
            }else{
                productDtoList = productDtoList.subList(0, pageNum*16);
            }
        }
//        Pageable pageRequest = PageRequest.of(0,16*pageNum);
//        Page<Product> productList = productRepository.findByPopular(pageRequest);


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

    //@Cacheable(key = "#category.concat(#pageNum)",value = "CategoryProduct",cacheManager = "CacheManager")
    public List<ProductDto> getCategoryProducts(String category, Integer pageNum) {
        List<ProductDto> productDtoList= new ArrayList<>();
        ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
        String Key= "CategoryProduct::"+category;
        if(redisData.get(Key)==null) { //전체 데이터 담기
            List<Product> products=productRepository.findByAddress(category);
            for(Product product: products){
                productDtoList.add(this.convertEntityToDto(product));
            }
            redisData.set(Key,productDtoList);
            redisTemplate.expire(Key,60L, TimeUnit.MINUTES);
        }else{
            log.info("getCategory_cache");
            productDtoList = (List<ProductDto>) redisData.get(Key);
            productDtoList.sort(new Comparator<ProductDto>() {
                @Override
                public int compare(ProductDto o1, ProductDto o2) {
                    return o2.getCreateDate().compareTo(o1.getCreateDate());
                }
            });
            if(pageNum*16>productDtoList.size()) {
                productDtoList = productDtoList.subList(0, productDtoList.size());
            }else{
                productDtoList = productDtoList.subList(0, pageNum*16);
            }
        }
        return  productDtoList;
//        Pageable pageRequest = PageRequest.of(0,16*pageNum);
//        Page<Product> products= productRepository.findByCategory(category,pageRequest);
//        for(Product product: products){
//            productDtoList.add(this.convertEntityToDto(product));
//        }
//        return  productDtoList;
    }

    //페이지 단위로 캐쉬를 적용했지만 새로운 데이터가 추가됬을때 어떻게 변경 해야할지 잘모르겠다.
    //일일이 캐쉬를 들어가면서 확인하는 것은 너무 비효율적인 방식이라고 생각이 돼서 방법을 바꿔야한다고 생각이 되었다.
    //전체 상품을 적재 시키고 거기서 추가시키는 방식으로 한다
    //pageNum을 이용해서 한정적으로 데이터를 보여주는 방식으로 바뀐다.
    //@Cacheable(key = "#address",value = "AddressProduct",condition = #pageNum < 1600 ,cacheManager = "CacheManager")
    public  List<ProductDto> getAddressProducts(String address,Integer pageNum) {
        List<ProductDto> productDtoList=new ArrayList<>();
        ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
        String Key= "AddressProduct::"+address;
        //log.info(String.valueOf(redisData.size(Key)));
        if(redisData.get(Key)==null) { //전체 데이터 담기
            //Pageable pageRequest = PageRequest.of(0,16*pageNum);
            //Page<Product> products= productRepository.findByAddress(address,pageRequest);
            List<Product> products=productRepository.findByAddress(address);
            for(Product product: products){
                productDtoList.add(this.convertEntityToDto(product));
            }
           redisData.set(Key,productDtoList);
            redisTemplate.expire(Key,60L, TimeUnit.MINUTES);
        }else{
            log.info("getAddress_cache");
            productDtoList = (List<ProductDto>) redisData.get(Key);
            productDtoList.sort(new Comparator<ProductDto>() {
                @Override
                public int compare(ProductDto o1, ProductDto o2) {
                    return o2.getCreateDate().compareTo(o1.getCreateDate());
                }
            });
            if(pageNum*16>productDtoList.size()) {
                productDtoList = productDtoList.subList(0, productDtoList.size());
            }else{
                productDtoList = productDtoList.subList(0, pageNum*16);
            }
        }
        return  productDtoList;
    }
    //ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
    //String Key=keyword+"::"+pageNum;
    //log.info(String.valueOf(redisData.get(Key)));
    //if(redisData.get(Key)==null) {
    //redisData.set(Key, productDtoList);
    //}else{
    //  log.info("getKeyword_cache");
    // productDtoList= (List<ProductDto>) redisData.get(Key);
    //}

    //@Cacheable(key = "#keyword.concat(#pageNum)",value = "KeywordProduct",cacheManager = "CacheManager")
    public List<ProductDto> getKeywordProducts(String keyword,Integer pageNum) {
        List<ProductDto> productDtoList = new ArrayList<>();
        String Key=keyword;
        ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
        //log.info(String.valueOf(redisData.get(Key)));
        if(redisData.get(Key)==null) {
            //JPA에서 limit을 사용하는 대신에 pageRequest를 사용해야 한다.
            Pageable pageRequest = PageRequest.of(0, 16 * pageNum);
            Page<Product> products = productRepository.findByKeyword('%' + keyword + '%', pageRequest);
            for (Product product : products) {
                productDtoList.add(this.convertEntityToDto(product));
            }
            log.info("getKeywordProducts");
            //redisData.set(Key, productDtoList);
        }else{
          //  log.info("getKeyword_cache");
            productDtoList= (List<ProductDto>) redisData.get(Key);
        }
       // System.out.println(redisData.get(Key));
        return  productDtoList;
    }

    /**
     *
     * @param product
     * @return
     * 사용목적 -
     */
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
                .address1(product.getUser().getAddress1())
                .address2(product.getUser().getAddress2())
                .categoryName(product.getCategory().getName())
                .build();
    }
    private PopularProductDto convertPopularEntityToDto(Product product) {
        return PopularProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .price(product.getPrice())
                .interestCount(product.getInterestCount())
                .status(product.getStatus())
                .address1(product.getUser().getAddress1())
                .address2(product.getUser().getAddress2())
                .chatCount(product.getChatRooms().size())
                .nickName(product.getUser().getNickName())
                .categoryName(product.getCategory().getName())
                .build();
    }

    //TODO multipart will solve
    //DetailProduct는 id값을 이용해서 해당 prodcut만 삭제하는 방향으로
    //KeywordProduct는 해당 keyword가 들어간 cache만 삭제를 한다. Keyword는 다양해서 캐시를 안하는게 날수도?
    //PopularProudct 10분주기로 해도 안늦을 것같다. 쉽게 바뀌지 않으니깐
    //AddressProudct  ADDPRODUCT에만 적용되는데 단순히 AddressProudct에 추가만 해주면될거같음
    //CategoryProudct categoryName을 이용해서 cache에 새로운 상품을 추가하는데 가장 앞에 추가하기
    @Transactional
    //@CacheEvict(cacheNames = {"DetailProduct","PopularProduct","CategoryProduct","KeywordProduct"},allEntries = true)
    public Product addProduct(String title, String content, LocalDateTime createDate, LocalDateTime updateDate,
                              Integer price, String status, String categoryName, String nickName, List<MultipartFile> files) throws IOException {

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
                .interestCount(0)
                .status(status)
                .category(category)
                .user(user)
                .build();
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
        product.setProductImages(productImageList);
        log.info(String.valueOf(productImageList));
        productRepository.save(product);
        CacheService cacheService=new CacheService();
        cacheService.deleteProductCache(user.getAddress1(), categoryName);
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
                .address1(product.getUser().getAddress1())
                .address2(product.getUser().getAddress2())
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
    @CacheEvict(cacheNames = {"DetailProduct","PopularProduct","AddressProduct","CategoryProduct","KeywordProduct"},allEntries = true)
    public Product updateProduct(Long id,String title, String content, LocalDateTime updateDate,
                                 Integer price, Integer interestCount, String status,String categoryName,String nickName,
                                List<MultipartFile> files) throws IOException {
        Product product= productRepository.findById(id).orElse(null);
        product.updateProduct(title,content,updateDate, price,interestCount,status);
        product.setCategory(categoryRepository.findByName(categoryName).orElse(null));
        if(!files.isEmpty()) {
            for (MultipartFile file : files) {
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
        }
        productRepository.save(product);

        return product;
    }

    @Transactional
    @CacheEvict(cacheNames = {"DetailProduct","PopularProduct","AddressProduct","CategoryProduct","KeywordProduct"},allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(cacheNames = {"DetailProduct","PopularProduct","AddressProduct","CategoryProduct","KeywordProduct"},allEntries = true)
    public void deleteImage(Long imageId) {
        ProductImage productImage= productImageRepository.findById(imageId).orElse(null);
        //productImage.setDeleteFlag(false);
        //productImageRepository.save(productImage);
        productImageRepository.deleteById(imageId);

    }

}
