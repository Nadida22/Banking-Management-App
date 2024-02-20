package com.banking.BankingApp.model;


import com.banking.BankingApp.model.enums.AccountType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;


@Entity
@Table(name="Account")
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

    public Account() {

    }

    public Account(Long accountId, AccountType accountType, Long accountNumber, BigDecimal balance, User user, Set<Transaction> transactions) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.user = user;
        this.transactions = transactions;
    }

    public Account(Long accountId, AccountType accountType, Long accountNumber, BigDecimal balance) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account(AccountType accountType, Long accountNumber, BigDecimal balance, Set<Transaction> transactions) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactions = transactions;
    }

    public Long getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public User getUser() {
        return user;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransactionToSet(Transaction transaction) {

        this.transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void removeTransactionFromSet(Transaction transaction) {

        this.transactions.remove(transaction);
        transaction.setAccount(null);
    }



    public static Long generateUniqueAccountNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;

    }

}


