package exrf.pos.service;

import exrf.pos.model.Category;
import exrf.pos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Optional<Category> getOne(Long id) {
        return categoryRepository.findById(id);
    }
}
