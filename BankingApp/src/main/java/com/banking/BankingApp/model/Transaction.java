package com.banking.BankingApp.model;


import com.banking.BankingApp.model.enums.TransactionStatus;
import com.banking.BankingApp.model.enums.TransactionType;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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


}
