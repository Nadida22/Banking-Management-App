package com.banking.BankingApp.model;

import com.banking.BankingApp.model.enums.AccountType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
//    accountId -- long -- unique identifier to reference account
//    accountType -- Enum -- Type of the account (e.g., savings, checking).
//    accountNumber -- long -- Unique number identifying the bank account. Visible to the user and shared for transactional purposes
//    balance -- BigDecimal -- Current balance of the account.
//    user -- User -- ManyToOne  userId -- long -- A reference to the associated User (foreign key).
//    transactions --HashSet -- OneToMany

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private Long accountNumber;
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions = new HashSet<>();





}


