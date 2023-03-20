package com.minty.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minty.dtorequest.OrderRequest;
import com.minty.entity.Order;
import com.minty.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
public class ConsumerOrderServiceImpl implements ConsumerOrderService{
    @Override
    public void getReport() {
    }
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ReportRepository reportRepository;

    @KafkaListener(topics = "abcTopic",concurrency = "thread1")
    public void listen(String message) throws JsonMappingException, JsonProcessingException {
        OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);
        log.info("Employee is : {}", orderRequest);
    }
    public Page<Order> reportOrder(String localDate, Pageable pageable){
      return reportRepository.giveMeAllOrderByDate(localDate,pageable) ;
    }
}
