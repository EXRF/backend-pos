package exrf.pos.service;

import exrf.pos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {
    @Autowired
    CategoryRepository categoryRepository;
}
