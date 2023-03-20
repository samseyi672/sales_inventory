package com.minty.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "customername")
    private String customername;
    @Column(name = "phonenumber")
    private String phonenumber;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "order")
    private List<OrderItem> orderItemList   = new ArrayList<>();
    @Column(name = "status")
    private String status;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "userId")
    private long userId;
    @Column(name = "totalprice")
    private BigDecimal totalPrice;  // this is totalamount
    @Column(name = "dateofcreation")
    private Date dateofcreation;
    @PrePersist
    protected void onCreate() {
        this.setDateofcreation(new Date());
    }
    @PreUpdate
    protected void onUpdate() {
        this.setDateofcreation(new Date());
    }
    public void addOrderItem(OrderItem orderItem){
       this.orderItemList.add(orderItem) ;
    }
    public void removeOrderItem(OrderItem orderItem){
        this.orderItemList.remove(orderItem) ;
    }
}
