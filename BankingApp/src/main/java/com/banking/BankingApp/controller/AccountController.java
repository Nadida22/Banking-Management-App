package com.banking.BankingApp.controller;


import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AccountController {

    private AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/account")
    public ResponseEntity<Account> registerNewAccount(@RequestBody Account account){

        Account result = accountService.RegisterAccount(account);

        try{
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidAccountException in){
            throw new InvalidAccountException("Check you input fields!!");
        }
    }


    @GetMapping("/account")
    public ResponseEntity<?> getAllAccounts(){

        List<Account> AllAccounts = accountService.findAllAccounts();
        return new ResponseEntity<>(AllAccounts, HttpStatus.OK);
    }



    @GetMapping("/accountId")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId){

        Account accountById;

        try{
            accountById = accountService.findAccountByAccountId(accountId);
        }catch(NotFoundException n){
            throw new NotFoundException("Account Id not found");
        }
        return new ResponseEntity<>(accountById, HttpStatus.OK);
    }


    @GetMapping("/user/{userId}/account")
    public List<Account> getAccountByUserId(@PathVariable Long userId){

        Account result = accountService.findAccountByAccountId(userId);

        return (List<Account>) new ResponseEntity<>(result, HttpStatus.OK);
    }

}
