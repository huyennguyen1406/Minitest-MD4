package cg.service;


import cg.model.Product;

import java.util.ArrayList;

public interface IProductService {
    ArrayList<Product> findAll();

    Product save(Product product);

    void delete(int id);

    Product findById(int id);

    ArrayList<Product> findAllByName(String name);

}
