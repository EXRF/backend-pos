package exrf.pos.service;

import exrf.pos.dto.requests.category.CreateCategoryRequestDto;
import exrf.pos.exception.ResourceNotFoundException;
import exrf.pos.model.Category;
import exrf.pos.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category getOne(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));

    }

    public void createOne(CreateCategoryRequestDto requestDto) {
        Optional<Category> optCategory = categoryRepository.findByName(requestDto.getName());
        if (optCategory.isPresent()) {
            throw new RuntimeException("Resource is exists!");
        }

        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        categoryRepository.save(category);
    }

    @Transactional
    public Category updateOne(Long id, CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found!"));

        if (requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            category.setName(requestDto.getName());
        }

        if (requestDto.getDescription() != null && !requestDto.getDescription().isEmpty()) {
            category.setDescription(requestDto.getDescription());
        }


        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        categoryRepository.save(category);
        return category;
    }

    @Transactional
    public void deleteOne(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));
        categoryRepository.delete(category);
    }
}
