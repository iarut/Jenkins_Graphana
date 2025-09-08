package org.example.repository;

import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Repository
public class ProductRepository {
    private List<Product> list = new ArrayList<Product>();

    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    public void createProducts() {
        logger.debug("Repository working on adding products product {}");
        list = List.of(
                new Product(1, "product 1", 10, 1000),
                new Product(2, "product 2", 20, 2000),
                new Product(3, "product 3", 30, 3000)
        );
    }

    public List<Product> getAllProducts() {
        logger.debug("Repository returning list of products product {}", list);
        return list;
    }

    public Product findById(int id){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == (id)) {
                logger.debug("Repository searching for item by id", id);
                return list.get(i);
            }
        }
        logger.info("No item found by id", id);
        return null;
    }

    public List<Product> search(String name) {
        logger.debug("Repository searching for item by name", name);
        return list.stream().filter(x -> x.getName().startsWith(name)).collect(Collectors.toList());
    }

    public Product save(Product p) {
        Product product = new Product();
        product.setId(p.getId());
        product.setName(p.getName());
        product.setQuantity(p.getQuantity());
        product.setPrice(p.getPrice());
        list.add(product);
        logger.debug("Repository adding new product", product);
        return product;
    }

    public String delete(Integer id) {
        list.removeIf(x -> x.getId() == (id));
        logger.debug("Repository deleting product by id", id);
        return null;
    }

    public Product update(Product product) {
        int idx = 0;
        int id = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == (product.getId())) {
                id = product.getId();
                idx = i;
                logger.info("Repository successfully updated product ", product);
                break;
            }
        }

        Product product1 = new Product();
        product1.setId(id);
        product1.setName(product.getName());
        product1.setQuantity(product.getQuantity());
        product1.setPrice(product.getPrice());
        list.set(idx, product);
        logger.debug("Repository added new product", product1);
        return product1;
    }
}