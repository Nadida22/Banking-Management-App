package com.banking.BankingApp.controller;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    // Added Autowired to accountService
    @Autowired
    private AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @PostMapping("/account")
    public ResponseEntity<Account> registerNewAccount(@RequestBody Account account) {
        // New accounts should require administrative authentication.
        Account result = accountService.registerAccount(account);
        // removed the exception, since validation and exception is being handled in service.
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }



    @GetMapping("/account")
    public ResponseEntity<?> getAllAccounts(){
        // getting all accounts should require administrative authentication.
        List<Account> AllAccounts = accountService.findAllAccounts();
        return new ResponseEntity<>(AllAccounts, HttpStatus.OK);
    }



    @GetMapping("/account/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId){
        // exception being handled in Service.
            Account accountById = accountService.findByAccountId(accountId);
            return new ResponseEntity<>(accountById, HttpStatus.OK);
    }


    @GetMapping("/user/{userId}/account")
    public ResponseEntity<Account> getAccountByUserId(@PathVariable Long userId){
        // changed the type returned from List<Account> to ResponseEntity<Account>
        Account result = accountService.findByAccountId(userId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
