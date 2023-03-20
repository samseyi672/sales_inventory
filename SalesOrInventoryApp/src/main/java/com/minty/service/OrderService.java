package com.minty.service;

import com.minty.entity.Order;
import com.minty.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    OrderItem save(OrderItem product);
    Order save(Order order);
}
