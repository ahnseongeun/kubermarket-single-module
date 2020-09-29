package com.example.kubermarket.service;

import com.example.kubermarket.domain.*;
import com.example.kubermarket.dto.ProductDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;
    public final CategoryRepository categoryRepository;
    public final ChatRoomRepository chatRoomRepository;
    public final UserRepository userRepository;

    public List<Product> getProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return  products;
    }

    @Transactional
    public List<ProductDto> getPopularProducts() {
        List<Object> objects= productRepository.findByInterestCountANDChatRoom();
        List<ProductDto> productDtoList= new ArrayList<>();
        for(Object object: objects){
            Object[] result= (Object[]) object;
            Product product= (Product) result[0];
            log.info(String.valueOf(Integer.parseInt(String.valueOf(result[1]))));
            productDtoList.add(this.convertEntityToDto(product));
        }
        return  productDtoList;

    }
    @Transactional
    public  List<ProductDto> getAddressProducts(String address) {
        List<Product> products= productRepository.findByAddress(address);
        List<ProductDto> productDtoList= new ArrayList<>();
        for(Product product: products){
            productDtoList.add(this.convertEntityToDto(product));
        }
        return  productDtoList;
    }


    @Transactional
    public List<ProductDto> getKeywordProducts(String keyword) {
        List<Product> products = productRepository.findByKeyword('%'+keyword+'%');
        List<ProductDto> productDtoList= new ArrayList<>();
        for(Product product: products){
            productDtoList.add(this.convertEntityToDto(product));
        }
        return  productDtoList;
    }

    private ProductDto convertEntityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .price(product.getPrice())
                .interestCount(product.getInterestCount())
                .status(product.getStatus())
                .address(product.getUser().getAddress1())
                .categoryName(product.getCategory().getName())
                .build();
    }

    String fileUrl =  System.getProperty("user.home") + "\\files";
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
        for(MultipartFile file :files) {
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
        return product;
    }



    public Product getDetailProduct(Long id) {
        return productRepository.findById(id).orElseThrow(); //해당 상품이 없다는 예외처리하기
        //TODO
    }

    public Product updateProduct(Long id,String title, String content, LocalDateTime updateDate,
                                 Integer price, Integer interestCount, String status,String categoryName,String nickName,
                                List<MultipartFile> files) throws IOException {
        Product product= productRepository.findById(id).orElse(null);
        product.updateProduct(title,content,updateDate, price,interestCount,status);
        product.setCategory(categoryRepository.findByName(categoryName).orElse(null));
        for(MultipartFile file :files) {
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
            productImageRepository.save(productImage);
        }
        productRepository.save(product);
        return product;
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteImage(Long imageId) {
        ProductImage productImage= productImageRepository.findById(imageId).orElse(null);
        //productImage.setDeleteFlag(false);
        //productImageRepository.save(productImage);
        productImageRepository.deleteById(imageId);

    }


}
