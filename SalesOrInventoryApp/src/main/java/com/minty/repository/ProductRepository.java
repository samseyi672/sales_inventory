package com.minty.repository;

import com.minty.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {
   @Query(value ="SELECT p from Product p")
   List<Product> findAll() ;
   @Query(value ="delect from product  where id=:id",nativeQuery = true)
   boolean deleteProduct(@Param("id") long id) ;
}
