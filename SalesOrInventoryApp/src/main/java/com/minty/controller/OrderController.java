package com.minty.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minty.dtorequest.OrderRequest;
import com.minty.entity.Order;
import com.minty.entity.OrderItem;
import com.minty.entity.Product;
import com.minty.service.OrderService;
import com.minty.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class OrderController {
    @Value("${api.version}")
    private String apiVersion ;
    @Autowired
    ProductService productService ;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    OrderService orderService ;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate ;

    @PostMapping("/order")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public ResponseEntity<String> placedOrderBasedOnAvailabilitOfProduct(@RequestBody OrderRequest orderRequest) throws JsonProcessingException {
      // List<Product> orderItemList  = Collections.synchronizedList(order.getOrderItemList().parallelStream().map(eachorder->eachorder.getProduct()).collect(Collectors.toList())); // convert to product list
      // List<Product> productListAvailableInStock =  orderItemList.stream().filter(p->productService.findById(p.getId()).isPresent()&&p.getNoOfItemInStock()!=0).map(p->p).collect(Collectors.toList());  // check availability
        Order order  = new Order() ;
        order.setCity(orderRequest.getCity());
        order.setCountry(orderRequest.getCountry());
        order.setCustomername(orderRequest.getCustomername());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setUserId(orderRequest.getUserId());
        order.setOrderItemList(orderRequest.getOrderItemList());
        List<OrderItem> orderItemList =  Collections.synchronizedList(order.getOrderItemList().parallelStream().filter(p->productService.findById(p.getProduct().getId()).isPresent()&&p.getProduct().getNoOfItemInStock()!=0).map(p->p).collect(Collectors.toList()));  // orderitem based on availability of product
        if(orderItemList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("The product is not available") ;
           }
        // process order item
        orderItemList.parallelStream().forEach(orderitem->orderService.save(orderitem));  // save order item
        order.setOrderItemList(orderItemList); // reset to available orderItem
        order.setStatus("Successful");
        orderService.save(order) ;
        CompletableFuture.runAsync(()->{
            try {
                kafkaTemplate.send("abcTopic","order",objectMapper.writeValueAsString(orderRequest)) ;//publish to Kafka to topic abc with key "order"
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });  //  run separate thread
        return ResponseEntity.status(HttpStatus.OK).body("The Order is placed Successfully") ;
    }
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}







































































