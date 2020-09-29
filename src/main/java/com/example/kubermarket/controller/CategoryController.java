package com.example.kubermarket.controller;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
    public List<Category> list(){
        List<Category> categories= categoryService.getCategories();

        return categories;
    }

    @ResponseBody
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ResponseEntity<?> create(
            @RequestBody Category resource
    ) throws URISyntaxException {
        String name = resource.getName();
        Category category = categoryService.addCategory(name);
        String url = "/api/category/" + category.getId();
        return ResponseEntity.created(new URI(url)).body("{}");
    }

    @ResponseBody
    @RequestMapping(value = "/category/{id}",method = RequestMethod.PATCH)
    public String update(@PathVariable("id") Long id,
                         @RequestBody Category resource
    ) throws URISyntaxException {
        String name = resource.getName();
        categoryService.updateCategory(id,name);
        return "{}";
    }

    @ResponseBody
    @RequestMapping(value = "/category/{id}",method = RequestMethod.DELETE)
    public String delete(
            @PathVariable("id") Long id
    ) throws URISyntaxException {
        categoryService.deleteCategory(id);
        return "{}";
    }

}
