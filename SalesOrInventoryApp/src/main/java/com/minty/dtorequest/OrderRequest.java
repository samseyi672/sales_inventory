package com.minty.dtorequest;

import com.minty.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String customername;
    private String phonenumber;
    private List<OrderItem> orderItemList   = new ArrayList<>();
    private String status;
    private String country;
    private String city;
    private long userId;
    private BigDecimal totalPrice;
}
