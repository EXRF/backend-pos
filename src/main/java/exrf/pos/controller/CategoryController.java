package exrf.pos.controller;

import exrf.pos.dto.requests.category.CreateCategoryRequestDto;
import exrf.pos.dto.responses.CommonResponseDto;
import exrf.pos.model.Category;
import exrf.pos.service.CategoryService;
import exrf.pos.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@Slf4j
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

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int pageSize) {
        if(page < 0 || pageSize <=0) {
            throw new IllegalArgumentException("Page must be 0 or greater, and pageSize must be greater than 0.");
        }

        Page<Category> categories = categoryService.getAll(page, pageSize);

        CommonResponseDto.Metadata metadata = new CommonResponseDto.Metadata(
                categories.getPageable().getPageNumber(),
                categories.getPageable().getPageSize(),
                categories.getTotalElements(),
                categories.getTotalPages()
        );

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(categories.getContent(), metadata, "Success"));
    }


    @PostMapping
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
