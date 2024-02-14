package com.banking.BankingApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.BankingApp.model.Transaction;
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {



}