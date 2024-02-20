package com.banking.BankingApp.repository;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.BankingApp.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {



    @Query("SELECT t FROM Transaction t WHERE t.account = :account")
    List<Transaction> findTransactionsByAccount(@Param("account") Account account);


    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.type = :type")
    List<Transaction> findTransactionsByAccountAndType(@Param("account") Account account, @Param("type") TransactionType type);


    @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByAccountAndDateRange(@Param("account") Account account, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);



}