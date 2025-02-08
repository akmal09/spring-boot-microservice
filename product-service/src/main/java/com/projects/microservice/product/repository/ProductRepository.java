package com.projects.microservice.product.repository;

import com.projects.microservice.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
//    List<Product> findAll(String category);
}
