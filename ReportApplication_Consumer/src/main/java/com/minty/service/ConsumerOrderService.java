package com.minty.service;


import com.minty.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public interface ConsumerOrderService {
    void getReport();
    Page<Order> reportOrder(String localDate, Pageable pageable) ;
}
