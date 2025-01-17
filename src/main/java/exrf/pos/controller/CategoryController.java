package exrf.pos.controller;

import exrf.pos.exception.ResourceNotFoundException;
import exrf.pos.model.Category;
import exrf.pos.service.CategoryService;
import exrf.pos.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Optional<Category> optCategory = categoryService.getOne(id);
        if (optCategory.isEmpty()) {
            throw new ResourceNotFoundException("Not Found");
        }

        Category category = new Category();

        return ResponseEntity.ok(
                ResponseUtil.responseSuccess(Category.class, category, "Success")
        );
    }

}
