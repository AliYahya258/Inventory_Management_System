package com.Inventory.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    private Erole roles;

    public User() {}

    public User(Long id, String username, String password, Erole roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    //Getter and Setter

}
