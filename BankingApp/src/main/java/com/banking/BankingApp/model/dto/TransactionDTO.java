package com.banking.BankingApp.model.dto;

import com.banking.BankingApp.model.enums.TransactionStatus;
import com.banking.BankingApp.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private Long recipientAccountNumber;
    private String description;
    // Instead of the whole Account object, you might want to include just the accountId or accountNumber
    private Long accountId;

    public TransactionDTO() {
    }

    public TransactionDTO(Long transactionId, BigDecimal amount, TransactionType type, TransactionStatus status, Long recipientAccount, String description, Long accountId) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.recipientAccountNumber = recipientAccount;
        this.description = description;
        this.accountId = accountId;
    }



    // Getters and Setters

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public void setRecipientAccountNumber(Long recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    // toString, equals, and hashCode methods can be added as needed
}
