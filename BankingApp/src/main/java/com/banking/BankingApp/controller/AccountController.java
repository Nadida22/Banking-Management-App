package com.banking.BankingApp.controller;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.dto.AccountDTO;
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



    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }




    @PostMapping("/account")
    public ResponseEntity<AccountDTO> registerNewAccount(@RequestBody AccountDTO accountDto) {
        // New accounts should require administrative authentication.
        AccountDTO response = accountService.registerAccount(accountDto);
        // removed the exception, since validation and exception is being handled in service.
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("/account")
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        // getting all accounts should require administrative authentication.
        List<AccountDTO> response = accountService.findAllAccounts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long accountId){
        // exception being handled in Service.
            AccountDTO response = accountService.findByAccountId(accountId);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/user/{userId}/account")
    public ResponseEntity<AccountDTO> getAccountByUserId(@PathVariable Long userId){
        // changed the type returned from List<Account> to ResponseEntity<Account>
        AccountDTO response = accountService.findByAccountId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
