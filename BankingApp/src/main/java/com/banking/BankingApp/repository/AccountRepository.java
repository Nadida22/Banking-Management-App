package com.banking.BankingApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.BankingApp.model.Account;
public interface AccountRepository extends JpaRepository<Account, Integer> {



}