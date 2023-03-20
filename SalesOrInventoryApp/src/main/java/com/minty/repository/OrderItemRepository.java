package com.minty.repository;

import com.minty.entity.OrderItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem,Long> {
}
