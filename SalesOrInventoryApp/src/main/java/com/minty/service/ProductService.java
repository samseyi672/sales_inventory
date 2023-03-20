package com.minty.service;

import com.minty.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ProductService {
   Page<Product>  getAllProduct(int pageNumber, int pageSize) ;
   Page<Product>  getAllProduct(Pageable pageable) ;
   Product save(Product product);
   Optional<Product> findById(long id);

   boolean update(Product p);

   boolean delete(long id);
}
