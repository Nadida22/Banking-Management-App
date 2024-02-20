package com.banking.BankingApp.model.dto;

import com.banking.BankingApp.model.Transaction;
import com.banking.BankingApp.model.enums.AccountType;

import java.math.BigDecimal;

import java.util.Objects;
import java.util.Set;

public class AccountDTO {

    private Long accountId;
    private AccountType accountType;
    private Long accountNumber;
    private BigDecimal balance;

    // userId instead of entire user object
    private Long userId;


    private Set<TransactionDTO> transactions;

    public AccountDTO() {
    }

    public AccountDTO(AccountType accountType, Long accountNumber, BigDecimal balance, Long userId) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountDTO(Long accountId, AccountType accountType, Long accountNumber, BigDecimal balance, Long userId) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;

    }




    public AccountDTO(Long accountId, AccountType accountType, Long accountNumber, BigDecimal balance, Long userId, Set<TransactionDTO> transactions) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
        this.transactions = transactions;
    }



    // Getters and Setters

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<TransactionDTO> getTransactions(){return transactions;}

    public void setTransactions(Set<TransactionDTO> transactions){
        this.transactions = transactions;
    }


    public void addTransactionToList(TransactionDTO transactionDto) {

        this.transactions.add(transactionDto);
    }
    public void removeTransactionFromList(TransactionDTO transactionDto) {

        this.transactions.remove(transactionDto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDTO that)) return false;
        return Objects.equals(accountId, that.accountId) && accountType == that.accountType && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(balance, that.balance) && Objects.equals(userId, that.userId) && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountType, accountNumber, balance, userId, transactions);
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountId=" + accountId +
                ", accountType=" + accountType +
                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", userId=" + userId +
                ", transactions=" + transactions +
                '}';
    }


}
