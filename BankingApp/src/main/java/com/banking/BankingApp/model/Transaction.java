package com.banking.BankingApp.model;


import com.banking.BankingApp.model.enums.TransactionStatus;
import com.banking.BankingApp.model.enums.TransactionType;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transaction {

    // TODO: Add fields



//    transactionId -- long -- A unique identifier for the transaction.
//    type -- Enum -- Type of transaction (e.g., deposit, withdrawal, transfer).
//    amount -- BigDecimal -- The amount of money involved in the transaction.
//    transactionDate -- DateTime -- The date and time when the transaction occurred.
//    status -- Enum -- Status of the transaction (e.g., pending, completed, failed).
//    recipientAccount -- long -- For transfers, the account number of the recipient (could be null for other types).
//    description -- String -- A very brief description or notes about the transaction.
//    account -- Account --  A reference to the associated Account.


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Long recipientAccount; // This can be null for non-transfer transactions
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction() {

    }

    public Transaction(TransactionType type, BigDecimal amount, LocalDateTime transactionDate, TransactionStatus status, Long recipientAccount, String description, Account account) {
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.recipientAccount = recipientAccount;
        this.description = description;
        this.account = account;
    }


    public Transaction(Long transactionId, TransactionType type, BigDecimal amount, LocalDateTime transactionDate, TransactionStatus status, Long recipientAccount, String description, Account account) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.recipientAccount = recipientAccount;
        this.description = description;
        this.account = account;
    }


    public Long getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Long getRecipientAccount() {
        return recipientAccount;
    }

    public String getDescription() {
        return description;
    }

    public Account getAccount() {
        return account;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setRecipientAccount(Long recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId) && type == that.type && Objects.equals(amount, that.amount) && Objects.equals(transactionDate, that.transactionDate) && status == that.status && Objects.equals(recipientAccount, that.recipientAccount) && Objects.equals(description, that.description) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, type, amount, transactionDate, status, recipientAccount, description, account);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", type=" + type +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", status=" + status +
                ", recipientAccount=" + recipientAccount +
                ", description='" + description + '\'' +
                ", account=" + account +
                '}';
    }
}
