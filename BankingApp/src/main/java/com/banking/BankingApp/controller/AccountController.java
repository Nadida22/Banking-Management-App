package com.banking.BankingApp.controller;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.LoginDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.service.AccountService;
import com.banking.BankingApp.service.LoginService;
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
    private LoginService loginService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }



    // OK
    @PostMapping("/account")
    public ResponseEntity<AccountDTO> registerNewAccount(@RequestBody LoginDTO<AccountDTO> loginAccountDto) {
        // Admin endpoint
        loginService.authenticateUser(loginAccountDto.getUsername(), loginAccountDto.getPassword(), UserRole.ADMIN);
        // New accounts should require administrative authentication.
        AccountDTO response = accountService.registerAccount(loginAccountDto.getData());
        // removed the exception, since validation and exception is being handled in service.
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }





    // OK
    @GetMapping("/account")
    public ResponseEntity<List<AccountDTO>> findAllAccounts(@RequestBody LoginDTO<?> loginDto){
        // Admin endpoint
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.ADMIN);
        // getting all accounts should require administrative authentication.
        List<AccountDTO> response = accountService.findAllAccounts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OK
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId, @RequestBody LoginDTO<?> loginDto){
        // Admin endpoint
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.ADMIN);
        boolean isDeleted = accountService.deleteAccount(accountId);
        if(!isDeleted)
            throw new NotFoundException("Account with id " + accountId + " was not found.");

        return new ResponseEntity<>("{\"message\":\"Successfully Deleted\"}", HttpStatus.OK);


    }

    // OK
    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable Long accountId, @RequestBody LoginDTO<?> loginDto){
        // exception being handled in Service.
            loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.USER);
            AccountDTO response = accountService.findByAccountId(accountId, loginDto.getUsername());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OK
    @GetMapping("/user/{userId}/account")
    public ResponseEntity<List<AccountDTO>> findAllAccountsByUserId(@PathVariable Long userId, @RequestBody LoginDTO<?> loginDto){
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.USER);
        List<AccountDTO> response = accountService.findAllAccountsByUserId(userId, loginDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    // OK
    @GetMapping("/user/{userId}/balance")
    public ResponseEntity<?> getTotalBalance(@PathVariable Long userId, @RequestBody LoginDTO<?> loginDto){
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.USER);
        BigDecimal response = accountService.findTotalBalance(userId, loginDto.getUsername());
        return new ResponseEntity<>("{\"balance\": " + response + " }", HttpStatus.OK);
    }


}
