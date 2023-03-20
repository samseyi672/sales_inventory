package com.minty.controller;


import com.minty.entity.Order;
import com.minty.service.ConsumerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConsumerOrderController {

    @Autowired
    ConsumerOrderService consumerOrderService ;

    @PostMapping("/report")
  public ResponseEntity<Page<Order>> report(@RequestParam("dateofreport")
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "dd.MM.yyyy") LocalDate localDate,Pageable pageable){
        String dateOfCreation  = localDate.getYear()+"-"+localDate.getMonth()+"-"+localDate.getDayOfMonth() ;
       return  ResponseEntity.ok(consumerOrderService.reportOrder(dateOfCreation,pageable))  ;
     }
}
