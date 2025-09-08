package org.example.service;

import org.example.controller.ProductController;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository repository;

    public Product saveProduct(Product product) {
        logger.debug("Service working on adding product {}", product);
        return repository.save(product);
    }

    public List<Product> getProducts() {
        logger.debug("Service returning list of products{}", repository.getAllProducts());
        return repository.getAllProducts();
    }

    public Product getProductById(int id) {
        logger.debug("Service searching for product {}", id);
        return repository.findById(id);
    }

    public String deleteProduct(int id) {
        repository.delete(id);
        logger.debug("Service deleting product {}", id);
        return "product removed !! " + id;
    }

    public Product updateProduct(Product product) {
        logger.debug("Service updating {}", product);
        return repository.update(product);
    }
}
