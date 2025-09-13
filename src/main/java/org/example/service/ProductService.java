package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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

    public Map<String, List<Product>> getProductsByName() {
        logger.debug("Service getting products by name");
        return repository.getProductsByName();
    }

    public Map<Double, List<Product>> getProductByPrice() {
        logger.debug("Service getting products by price ");
        return repository.getProductsByPrice();
    }

    public Map<Integer, List<Product>> getProductsByQuantity() {
        logger.debug("Service getting products by quantity ");
        return repository.getProductsByQuantity();
    }

    public Map<Integer, List<Product>> getProductsById() {
        logger.debug("Service getting products by id ");
        return repository.getProductsById();
    }
}
