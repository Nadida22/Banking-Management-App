package com.banking.BankingApp.controller;
import com.banking.BankingApp.model.dto.LoginDTO;
import com.banking.BankingApp.model.dto.TransactionDTO;
import com.banking.BankingApp.model.dto.TransactionDatesDTO;
import com.banking.BankingApp.model.enums.TransactionType;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.service.LoginService;
import com.banking.BankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/transaction/{accountId}")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionByAccountId(@PathVariable Long accountId, @RequestBody LoginDTO<?> loginDto){
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.USER);
        List<TransactionDTO> response = transactionService.findTransactionsByAccountId(accountId, loginDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/transaction/{accountId}/{type}")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionByAccountIdAndType(@PathVariable Long accountId, @PathVariable String type, @RequestBody LoginDTO<?> loginDto){
        TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.USER);
        List<TransactionDTO> response = transactionService.findTransactionsByAccountIdAndType(accountId, loginDto.getUsername(), transactionType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable Long transactionId, @RequestBody LoginDTO<?> loginDto){
        // exception being handled in Service.
        loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.ADMIN);
        TransactionDTO response = transactionService.findByTransactionId(transactionId, loginDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/transaction/{accountId}/filterByDate")
    public ResponseEntity<List<TransactionDTO>> findAllTransactionByAccountIdAndDate(@PathVariable Long accountId, @RequestBody LoginDTO<TransactionDatesDTO> loginDatesDTO){
        loginService.authenticateUser(loginDatesDTO.getUsername(), loginDatesDTO.getPassword(), UserRole.USER);
        TransactionDatesDTO dates = loginDatesDTO.getData();
        List<TransactionDTO> response = transactionService.findTransactionsByAccountIdAndDate(accountId, loginDatesDTO.getUsername(), dates.getStartDate(), dates.getEndDate());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






}
