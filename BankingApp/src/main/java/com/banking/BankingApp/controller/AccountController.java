package com.banking.BankingApp.controller;
import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.TokenDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.service.AccountService;
import com.banking.BankingApp.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
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
    public ResponseEntity<AccountDTO> registerNewAccount(@RequestBody TokenDTO<AccountDTO> tokenDto) {
        // Admin endpoint
        UserRole requiredRole = UserRole.ADMIN;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        AccountDTO response = accountService.registerAccount(tokenDto.getData());
        // removed the exception, since validation and exception is being handled in service.
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }





    // OK
    @GetMapping("/account")
    public ResponseEntity<List<AccountDTO>> findAllAccounts(@RequestBody TokenDTO<?> tokenDto){
        // Admin endpoint
        UserRole requiredRole = UserRole.ADMIN;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        // getting all accounts should require administrative authentication.
        List<AccountDTO> response = accountService.findAllAccounts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OK
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId, @RequestBody TokenDTO<?> tokenDto){
        // Admin endpoint
        UserRole requiredRole = UserRole.ADMIN;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        boolean isDeleted = accountService.deleteAccount(accountId);
        if(!isDeleted)
            throw new NotFoundException("Account with id " + accountId + " was not found.");

        return new ResponseEntity<>("{\"message\":\"Successfully Deleted\"}", HttpStatus.OK);


    }

    // OK
    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable Long accountId, @RequestBody TokenDTO<AccountDTO> tokenDto){
        // exception being handled in Service.
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
            AccountDTO response = accountService.findByAccountId(accountId, tokenDto.getUsername());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OK
    @GetMapping("/user/{userId}/account")
    public ResponseEntity<List<AccountDTO>> findAllAccountsByUserId(@PathVariable Long userId, @RequestBody TokenDTO<?> tokenDto){
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        List<AccountDTO> response = accountService.findAllAccountsByUserId(userId, tokenDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    // OK
    @GetMapping("/user/{userId}/balance")
    public ResponseEntity<?> getTotalBalance(@PathVariable Long userId, @RequestBody TokenDTO<Long> tokenDto){
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        BigDecimal response = accountService.findTotalBalance(userId, tokenDto.getUsername());
        return new ResponseEntity<>("{\"balance\": " + response + " }", HttpStatus.OK);
    }


}
