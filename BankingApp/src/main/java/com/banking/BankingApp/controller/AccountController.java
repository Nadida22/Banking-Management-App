package com.banking.BankingApp.controller;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.UserDTO;
import com.banking.BankingApp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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



    // OK
    @PostMapping("/account")
    public ResponseEntity<AccountDTO> registerNewAccount(@RequestBody AccountDTO accountDto) {
        // New accounts should require administrative authentication.
        AccountDTO response = accountService.registerAccount(accountDto);
        // removed the exception, since validation and exception is being handled in service.
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }





    // OK
    @GetMapping("/account")
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        // getting all accounts should require administrative authentication.
        List<AccountDTO> response = accountService.findAllAccounts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OK
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId, @RequestBody UserDTO adminUserDto){
        boolean isDeleted = accountService.deleteAccount(accountId, adminUserDto);
        if(!isDeleted)
            throw new NotFoundException("Account with id " + accountId + " was not found.");

        return new ResponseEntity<>("{\"message\":\"Successfully Deleted\"}", HttpStatus.OK);


    }

    // OK
    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable Long accountId){
        // exception being handled in Service.
            AccountDTO response = accountService.findByAccountId(accountId);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OK
    @PostMapping("/user/{userId}/account")
    public ResponseEntity<List<AccountDTO>> findAllAccountsByUserId(@PathVariable Long userId, @RequestBody UserDTO userDTO){
        List<AccountDTO> response = accountService.findAccountsByUserId(userId, userDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    // OK
    @PostMapping("/user/{userId}/balance")
    public ResponseEntity<?> getTotalBalance(@PathVariable Long userId, @RequestBody UserDTO userDTO){
        BigDecimal response = accountService.findTotalBalance(userId, userDTO);
        return new ResponseEntity<>("{\"balance\": " + response + " }", HttpStatus.OK);
    }


}
