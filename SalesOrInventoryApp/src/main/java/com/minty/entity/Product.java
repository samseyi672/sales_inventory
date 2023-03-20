package com.minty.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "product")
@Cacheable
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "productname")
    private String productname;
    @Column(name = "productprice")
    private BigDecimal productprice;
    @Column(name = "noofiteminstock")
    private long noOfItemInStock;
    @Column(name = "productdescription")
    private String productdescription;
    @Column(name = "dateofcreation")
    private Date dateofcreation;

    @PrePersist
    protected void onCreate() {
        this.dateofcreation = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateofcreation = new Date();
    }
}






