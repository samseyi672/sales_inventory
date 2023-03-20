package com.minty.service;

import com.minty.entity.Product;
import com.minty.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
     @Autowired
    ProductRepository productRepository ;

     @Autowired
    JdbcTemplate jdbcTemplate ;
    @Override
    public Page<Product> getAllProduct(int pageNumber, int pageSize) {
        Pageable pageable  = PageRequest.of(pageNumber,pageSize) ;
        return productRepository.findAll(pageable) ;
    }
    @Override
    public Page<Product> getAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable) ;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product) ;
    }

    @Override
    public Optional<Product> findById(long id) {
        return  productRepository.findById(id) ;
    }

    @Override
    public boolean update(Product product) {
        return productRepository.findById(product.getId()).isPresent()?true:false ;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update("DELETE FROM product WHERE id = ?", id) == 1;
    }
}




