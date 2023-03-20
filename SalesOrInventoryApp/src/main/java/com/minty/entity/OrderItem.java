package com.minty.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "orderItem")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    private Product product ;
    @Column(name = "quantity")
    private int quantity ;
    @ManyToOne
    private Order order ;
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
