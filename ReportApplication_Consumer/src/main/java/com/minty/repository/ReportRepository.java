package com.minty.repository;

import com.minty.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends PagingAndSortingRepository<Order,Long> {
    @Query(value ="SELECT count(*) as Total_Order,totalprice as Total_Order_Amount,dateofcreation as Date FROM order where dateofcreation=:dateofcreation order by dateofcreation", nativeQuery = true)
    Page<Order> giveMeAllOrderByDate(@Param("dateofcreation") String dateofcreation, Pageable pageable) ;
}
