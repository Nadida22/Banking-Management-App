package com.banking.BankingApp.model;

import com.banking.BankingApp.model.enums.UserRole;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
public class User {

    // TODO: add fields



    // userid -- long - unique identifier
    // role -- enum -  to distinguish between user role or admin role
    // username -- String - self-explanatory
    // password -- String
    // email -- String
    // firstname -- String
    // lastname -- String
    // accounts -- HashSet -- OnetoMany relationship to Account
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();




}
