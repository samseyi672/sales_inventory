package com.minty.service;


import com.minty.entity.Order;
import com.minty.entity.OrderItem;
import com.minty.repository.OrderItemRepository;
import com.minty.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository ;

    @Autowired
    OrderItemRepository orderItemRepository ;
    @Override
    public OrderItem save(OrderItem orderItem) {
        return  orderItemRepository.save(orderItem) ;
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
