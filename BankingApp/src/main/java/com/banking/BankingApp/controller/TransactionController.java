package com.banking.BankingApp.controller;

import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.LoginDTO;
import com.banking.BankingApp.model.dto.TransactionDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.service.LoginService;
import com.banking.BankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    LoginService loginService;



    @PostMapping("/transaction")
    public ResponseEntity<TransactionDTO> registerNewTransaction(@RequestBody LoginDTO<TransactionDTO> loginTransactionDto) {
        loginService.authenticateUser(loginTransactionDto.getUsername(), loginTransactionDto.getPassword(), UserRole.USER);
        TransactionDTO transactionDto = loginTransactionDto.getData();
        TransactionDTO response = transactionService.createTransaction(transactionDto, loginTransactionDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionDTO>> findAllTransactions(@RequestBody LoginDTO<?> loginDto) {
        // Admin endpoint
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.ADMIN);
        // getting all accounts should require administrative authentication.
        List<TransactionDTO> response = transactionService.findAllTransactions();
        return new ResponseEntity<>(response, HttpStatus.OK);


    }




}
