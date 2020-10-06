package com.example.kubermarket.controller;

import com.example.kubermarket.domain.Product;
import com.example.kubermarket.domain.ProductImage;
import com.example.kubermarket.dto.AddressProductDto;
import com.example.kubermarket.dto.PopularProductDto;
import com.example.kubermarket.dto.ProductDto;
import com.example.kubermarket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/api")
public class ProductController {

    public final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseBody //모든 상품을 조회할수 있는 관리자 기능
    @RequestMapping(value = "/allproducts",method = RequestMethod.GET)
    public List<Product> AllProductList(){
        List<Product> products = productService.getProducts();

        return products;
    }

    @ResponseBody // 일반 유저가 조회할수 있는 기능
    @RequestMapping(value = "/product/{id}",method = RequestMethod.GET)
    public ProductDto DetailProduct(
            @Valid @PathVariable Long id){
        ProductDto productDto= productService.getDetailProduct(id);
        return productDto;
    }

    @ResponseBody // 일반 유저가 조회할수 있는 기능
    @RequestMapping(value = "/products",method = RequestMethod.GET)
    public List<? extends Object> productList(
            @RequestParam(value="popular", defaultValue = "true",required = false) boolean popular, //interest_count와 chat_count를 합산해서 보여준다.
            @RequestParam(value="address", defaultValue = "false",required = false) boolean address, //address와 registerDate를 조합해서 보여준다.
            @RequestParam(value="requestaddress", defaultValue = "songpa",required = false) String requestaddress, //address와 registerDate를 조합해서 보여준다.
            @RequestParam(value="keyword", defaultValue = "",required = false) String keyword, //이름이나 동네로 검색
            @RequestParam(value="page", defaultValue = "1",required = false) Integer pageNum ){
        if(popular) { //인기 항목순으로 조회
            List<PopularProductDto> productList = productService.getPopularProducts();
            //List<ProductDto> ProductDtoList = new ArrayList<>();
//            for (Object o : productList) {
//                Object[] result = (Object[]) o;
//                PopularProductDto popularProductDto = new PopularProductDto(
//                        Long.parseLong(String.valueOf(result[0])), Long.parseLong(String.valueOf(result[1])));
//                popularProductDtoList.add(popularProductDto);
//            }
            return  productList;
        }else if(address){ //주소+최신으로 조회
            List<ProductDto>  productDtoList  = productService.getAddressProducts(requestaddress);
           // List<AddressProductDto> addressProductDtoList = new ArrayList<>();
//            for(Object o : productList){
//                Object[] result= (Object[]) o;
//                AddressProductDto addressProductDto = new AddressProductDto(
//                        Long.parseLong(String.valueOf(result[0])), (LocalDateTime) result[1],
//                        String.valueOf(result[2])
//                );
//                addressProductDtoList.add(addressProductDto);
//            }
            return productDtoList;
        }else{ //키워드로 조회

            List<ProductDto>  productDtoList = productService.getKeywordProducts(keyword);
            return productDtoList;
        }

    }

    @ResponseBody
    @RequestMapping(value = "/product",method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(
            @Valid
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("price") Integer price,
            @RequestParam("interestCount") Integer interestCount,
            @RequestParam("status") String status,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("nickName") String nickName,
            @RequestParam(value = "files",required = false) List<MultipartFile> files
            ) throws URISyntaxException, IOException {

        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime updateDate = LocalDateTime.now();
        Product product = productService.addProduct(title, content, createDate, updateDate, price, interestCount, status, categoryName, nickName, files);
        String url = "/api/product/" + product.getId();
        return ResponseEntity.created(new URI(url)).body("{}");

    }

    @ResponseBody
    @RequestMapping(value = "/product/{id}",method = RequestMethod.PATCH)
    public String update(@PathVariable("id") Long id,
                         @Valid
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         @RequestParam("price") Integer price,
                         @RequestParam("interestCount") Integer interestCount,
                         @RequestParam("status") String status,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("nickName") String nickName,
                         @RequestParam(value = "files",required = false) List<MultipartFile> files
    ) throws URISyntaxException, IOException {
        LocalDateTime updateDate = LocalDateTime.now();
        Product product = productService.updateProduct(id,title, content,updateDate,price,interestCount,status,
                                                        categoryName,nickName,files);
        return "{수정완료}";
    }

    @ResponseBody
    @RequestMapping(value = "/product/{id}",method = RequestMethod.DELETE)
    public String delete(
            @PathVariable("id") Long id
    ) throws URISyntaxException {
       productService.deleteProduct(id);
        return "{Product 삭제완료}";
    }

    @ResponseBody
    @RequestMapping(value = "/productImage/{imageId}",method = RequestMethod.DELETE)
    public String deleteImage(
            @PathVariable("imageId") Long imageId
    ) throws URISyntaxException {
        productService.deleteImage(imageId);
        return "{ProductImage 삭제완료}";
    }

}
