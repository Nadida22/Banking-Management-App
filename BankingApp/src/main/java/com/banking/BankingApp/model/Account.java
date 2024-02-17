package com.banking.BankingApp.model;

import com.banking.BankingApp.model.enums.AccountType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Column(name="account_id")
    private Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(name="account_type")
    private AccountType accountType;

    @Column(name="account_number")
    private Long accountNumber;

    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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

    public Account(AccountType accountType, Long accountNumber, BigDecimal balance, User user, Set<Transaction> transactions) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.user = user;
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


    public static Account sanitize(Account account){

        Account sanitizedAccount = new Account();
        sanitizedAccount.setAccountId(account.getAccountId());
        sanitizedAccount.setAccountType(account.getAccountType());
        sanitizedAccount.setAccountNumber(account.getAccountNumber());
        sanitizedAccount.setBalance(account.getBalance());
        // user and transactions not included.

        return sanitizedAccount;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) && accountType == account.accountType && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance) && Objects.equals(user, account.user) && Objects.equals(transactions, account.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountType, accountNumber, balance, user, transactions);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountType=" + accountType +
                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", user=" + user +
                ", transactions=" + transactions +
                '}';
    }
}


