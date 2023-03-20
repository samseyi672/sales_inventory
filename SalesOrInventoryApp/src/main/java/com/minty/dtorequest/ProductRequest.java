package com.minty.dtorequest;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String productname;
    private BigDecimal productprice;
    private long noOfItemInStock;
    private BigDecimal productdescription; //  for money
}
