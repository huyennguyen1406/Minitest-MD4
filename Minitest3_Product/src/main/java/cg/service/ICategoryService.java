package cg.service;

import cg.model.Category;

public interface ICategoryService {

    Iterable<Category> findAll();

    Category findById(Long id);

    void deleteById(Long id);

    Category save(Category category);
}
