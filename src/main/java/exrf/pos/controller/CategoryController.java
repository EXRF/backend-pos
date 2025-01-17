package exrf.pos.controller;

import exrf.pos.dto.requests.category.CreateCategoryRequestDto;
import exrf.pos.model.Category;
import exrf.pos.service.CategoryService;
import exrf.pos.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Category category = categoryService.getOne(id);

        return ResponseEntity.ok(
                ResponseUtil.responseSuccess(Category.class, category, "Success")
        );
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createOne(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        categoryService.createOne(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.responseSuccess(CategoryController.class, "Created"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOne(@PathVariable("id") Long id, @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        Category category = categoryService.updateOne(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(Category.class,category ,"Updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
        categoryService.deleteOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(CategoryController.class, "Deleted"));
    }
}
