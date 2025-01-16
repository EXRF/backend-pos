package exrf.pos.controller;

import exrf.pos.dto.requests.category.CreateCategoryRequestDto;
import exrf.pos.repository.CategoryRepository;
import exrf.pos.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JwtUtils jwtUtils;

}
