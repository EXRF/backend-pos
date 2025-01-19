package exrf.pos.service;

import exrf.pos.dto.requests.product.CreateProductRequestDto;
import exrf.pos.exception.ResourceNotFoundException;
import exrf.pos.model.Category;
import exrf.pos.model.Product;
import exrf.pos.repository.CategoryRepository;
import exrf.pos.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Product getOne(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
    }

    public Page<Product> getAll (int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return productRepository.findAll(pageable);
    }

    public void createOne(CreateProductRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category with ID " + requestDto.getCategoryId() + " not fond"));

        Product product = new Product();
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setSku(requestDto.getSku());
        product.setPrice(requestDto.getPrice());
        product.setCategory(category);

        productRepository.save(product);
    }

    @Transactional
    public Product updateOne(Long id, CreateProductRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catetofy with ID " + requestDto.getCategoryId() + " not found"));

        if (requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            product.setName(requestDto.getName());
        }

        if (requestDto.getDescription() != null && !requestDto.getDescription().isEmpty()) {
            product.setDescription(requestDto.getDescription());
        }

        if(requestDto.getPrice() != null && requestDto.getPrice() != 0){
            product.setPrice(requestDto.getPrice());
        }

        if(requestDto.getSku() != null && requestDto.getSku().isEmpty()) {
            product.setSku(requestDto.getSku());
        }

        if(category != null && category.getId() != 0) {
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteOne(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));

        productRepository.delete(product);
    }
}
