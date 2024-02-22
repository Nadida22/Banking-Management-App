package com.banking.BankingApp.controller;
import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.model.dto.TokenDTO;
import com.banking.BankingApp.model.dto.TransactionDTO;
import com.banking.BankingApp.model.dto.TransactionDatesDTO;
import com.banking.BankingApp.model.enums.TransactionType;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.service.LoginService;
import com.banking.BankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class TransactionController {



    @Autowired
    TransactionService transactionService;

    @Autowired
    LoginService loginService;



    @PostMapping("/transaction")
    public ResponseEntity<TransactionDTO> registerNewTransaction(@RequestBody TokenDTO<TransactionDTO> tokenDto) {
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        TransactionDTO transactionDto = tokenDto.getData();
        TransactionDTO response = transactionService.createTransaction(transactionDto, tokenDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionDTO>> findAllTransactions(@RequestBody TokenDTO<?> tokenDto) {
        // Admin endpoint
        UserRole requiredRole = UserRole.ADMIN;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        List<TransactionDTO> response = transactionService.findAllTransactions();
        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @GetMapping("/transaction/{accountId}")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionsByAccountId(@PathVariable Long accountId, @RequestBody TokenDTO<?> tokenDto){
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        List<TransactionDTO> response = transactionService.findTransactionsByAccountId(accountId, tokenDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/transaction/{accountId}/{type}")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionsByAccountIdAndType(@PathVariable Long accountId, @PathVariable String type, @RequestBody TokenDTO<?> tokenDto){
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
        List<TransactionDTO> response = transactionService.findTransactionsByAccountIdAndType(accountId, tokenDto.getUsername(), transactionType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable Long transactionId, @RequestBody TokenDTO<?> tokenDto){
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        TransactionDTO response = transactionService.findByTransactionId(transactionId, tokenDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/transaction/{accountId}/filterByDate")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionsByAccountIdAndDate(@PathVariable Long accountId, @RequestBody TokenDTO<TransactionDatesDTO> tokenDto){
        UserRole requiredRole = UserRole.USER;
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        TransactionDatesDTO dates = tokenDto.getData();
        List<TransactionDTO> response = transactionService.findTransactionsByAccountIdAndDate(accountId, tokenDto.getUsername(), dates.getStartDate(), dates.getEndDate());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






}
