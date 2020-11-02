package com.example.kubermarket.controller;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.dto.CategoryDto;
import com.example.kubermarket.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping(value = "/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService=categoryService;
    }

    @ResponseBody
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ApiOperation(value = "Category List(client)", notes = "카테고리 목록 불러오기")
    public List<CategoryDto> list(){ //DTO 처리 해야한다.
        List<CategoryDto> categoryDtoList= categoryService.getCategories();
        return categoryDtoList;
    }


    @ResponseBody
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    @ApiOperation(value = "Category Save(관리자 기능)", notes = "카테고리 저장")
    public ResponseEntity<?> create(
            @RequestParam(value="category", defaultValue = "false",required = false) String name
    ) throws URISyntaxException {
        Category category = categoryService.addCategory(name);
        String url = "/api/category/" + category.getId();
        return ResponseEntity.created(new URI(url)).body("{}");
    }

    @ResponseBody
    @RequestMapping(value = "/category/{id}",method = RequestMethod.PATCH)
    @ApiOperation(value = "Category Update(관리자 기능)", notes = "카테고리 수정")
    public Category update(@PathVariable("id") Long id,
                           @RequestParam(value="category", defaultValue = "false",required = false) String name
    ) throws URISyntaxException {
        return categoryService.updateCategory(id,name);
    }

    @ResponseBody
    @RequestMapping(value = "/category/{id}",method = RequestMethod.DELETE)
    @ApiOperation(value = "Category delete(관리자 기능)", notes = "카테고리 삭제")
    public String delete(
            @PathVariable("id") Long id
    ) throws URISyntaxException {
        categoryService.deleteCategory(id);
        return "{}";
    }

}
