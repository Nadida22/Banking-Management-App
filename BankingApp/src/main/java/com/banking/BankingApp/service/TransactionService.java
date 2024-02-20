package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.Transaction;
import com.banking.BankingApp.model.dto.TransactionDTO;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;


@Service
public class TransactionService {



    @Autowired
    TransactionRepository transactionRepository;


    @Autowired
    AccountRepository accountRepository;


    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    // convert to DTO
    public TransactionDTO convertToDTO(Transaction transaction){
        // new TransactionDTO
        logger.info(transaction.toString());
        TransactionDTO transactionDto = new TransactionDTO();
        transactionDto.setTransactionId(transaction.getTransactionId());
        transactionDto.setType(transaction.getType());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setRecipientAccountId(transaction.getRecipientAccountId());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setAccountId(transaction.getAccount().getAccountId());
        return transactionDto;
        }


    // convert to Entity
    public Transaction convertToEntity(TransactionDTO transactionDto) {
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account Not Found."));
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setType(transaction.getType());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setRecipientAccountId(transactionDto.getRecipientAccountId());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAccount(account);
        return transaction;
    }


//    transactionId -- long -- A unique identifier for the transaction.
//    type -- Enum -- Type of transaction (e.g., deposit, withdrawal, transfer).
//    amount -- BigDecimal -- The amount of money involved in the transaction.
//    transactionDate -- DateTime -- The date and time when the transaction occurred.
//    status -- Enum -- Status of the transaction (e.g., pending, completed, failed).
//    recipientAccount -- long -- For transfers, the account number of the recipient (could be null for other types).
//    description -- String -- A very brief description or notes about the transaction.
//    account -- Account --  A reference to the associated Account.




    // get transaction by id


    // create new transaction


    // get all transactions for account


    // get all transactions for account by type, date, or amount


    // delete transaction by id ???






}
