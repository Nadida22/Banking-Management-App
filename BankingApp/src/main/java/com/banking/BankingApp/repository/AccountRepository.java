package com.banking.BankingApp.repository;
import com.banking.BankingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.BankingApp.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query("SELECT a FROM Account a WHERE a.user = :user")
    Optional<Account> findAccountsByUser(@Param("user") User user);


}