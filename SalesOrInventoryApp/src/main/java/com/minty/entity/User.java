package com.minty.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
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
