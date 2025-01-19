package exrf.pos.controller;

import exrf.pos.dto.requests.product.CreateProductRequestDto;
import exrf.pos.dto.responses.CommonResponseDto;
import exrf.pos.model.Product;
import exrf.pos.service.ProductService;
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
@RequestMapping("/api/product")
@Slf4j
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Product product = productService.getOne(id);

        return ResponseEntity.ok(
                ResponseUtil.responseSuccess(Product.class, product, "Success")
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Page must be 1 or greater, and pageSize must be greater than 0.");
        }

        Page<Product> products = productService.getAll(page, pageSize);

        CommonResponseDto.Metadata metadata = new CommonResponseDto.Metadata(
                page,
                products.getPageable().getPageSize(),
                products.getTotalElements(),
                products.getTotalPages()
        );

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(products.getContent(), metadata, "Success"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createOne(@RequestBody @Valid CreateProductRequestDto requestDto) {
        productService.createOne(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.responseSuccess(ProductController.class, "Created"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOne(@PathVariable("id") Long id, @RequestBody @Valid CreateProductRequestDto requestDto) {
        Product product = productService.updateOne(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(Product.class, product, "Updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
        productService.deleteOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.responseSuccess(ProductController.class, "Deleted"));
    }
}
