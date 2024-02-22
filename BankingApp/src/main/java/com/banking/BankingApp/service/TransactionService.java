package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidTransactionException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.Transaction;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.TransactionDTO;
import com.banking.BankingApp.model.dto.UserDTO;
import com.banking.BankingApp.model.enums.TransactionStatus;
import com.banking.BankingApp.model.enums.TransactionType;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.TransactionRepository;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.validator.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TransactionService {



    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionValidator transactionValidator;


    @Autowired
    LoginService loginService;


    // Needs more testing and more routes


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
        transactionDto.setRecipientAccountNumber(transaction.getRecipientAccountNumber());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setAccountId(transaction.getAccount().getAccountId());
        transactionDto.setStatus(transaction.getStatus());
        return transactionDto;
        }


    // convert to Entity
    public Transaction convertToEntity(TransactionDTO transactionDto) {
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account Not Found."));
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setType(transactionDto.getType());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setRecipientAccountNumber(transactionDto.getRecipientAccountNumber());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAccount(account);
        transaction.setStatus(transactionDto.getStatus());
        return transaction;
    }



    private void validate(Transaction transaction) throws InvalidTransactionException{
        Errors errors = new BeanPropertyBindingResult(transaction, "transaction");
        transactionValidator.validate(transaction, errors);
        if (errors.hasErrors()) {
            throw new InvalidTransactionException("Transaction is Invalid.", errors);
        }

    }




    // get transaction by id


    public boolean isBalanceValid(BigDecimal balance, BigDecimal amount){
        return amount.compareTo(BigDecimal.ZERO) >= 0 && balance.compareTo(amount) >= 0;
    }

    // create new transaction

    public TransactionDTO createTransaction(TransactionDTO transactionDto, String activeUsername){

        User activeUser = userRepository.findByUsername(activeUsername)
                .orElseThrow(() -> new NotFoundException("Can't retrieved active account."));

        Transaction mainTransaction = convertToEntity(transactionDto);
        validate(mainTransaction);

        mainTransaction.setTransactionDate(LocalDateTime.now());
        TransactionType role = mainTransaction.getType();

        Account originalAccount = mainTransaction.getAccount();
        accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account Not Found."));



        BigDecimal originalBalance = originalAccount.getBalance();


        loginService.checkPrivileges(originalAccount.getUser().getUserId(), activeUser);
        if(role == TransactionType.WITHDRAWAL || role ==TransactionType.TRANSFER){
            if(!isBalanceValid(originalBalance, mainTransaction.getAmount())){
                mainTransaction.setStatus(TransactionStatus.FAILED);
                throw new InvalidTransactionException("Insufficient Funds. Transaction Not Processed.");
            }
        }

        if(role == TransactionType.DEPOSIT || role == TransactionType.WITHDRAWAL ){
            processSimpleTransaction(mainTransaction, originalAccount, originalBalance);
        } else{
            processTransfer(mainTransaction, originalAccount, originalBalance);
        }

        accountRepository.save(originalAccount);
        transactionRepository.save(mainTransaction);
        return convertToDTO(mainTransaction);

    }

    public void processSimpleTransaction(Transaction transaction, Account account, BigDecimal balance){
        BigDecimal newBalance = (transaction.getType() == TransactionType.DEPOSIT) ? balance.add(transaction.getAmount()) : balance.subtract(transaction.getAmount());
        account.setBalance(newBalance);
        transaction.setStatus(TransactionStatus.COMPLETED);
        accountRepository.save(account);
        transactionRepository.save(transaction);


    }

    public void processTransfer(Transaction transaction, Account originalAccount, BigDecimal originalBalance) throws NotFoundException{
        Account recipientAccount = accountRepository.findById(transaction.getRecipientAccountNumber())
                .orElseThrow(() -> new NotFoundException("Recipient Account Not Found."));
        BigDecimal newOriginalBalance = originalBalance.subtract(transaction.getAmount());

        // get recipient balance.
        BigDecimal recipientOldBalance = recipientAccount.getBalance();
        BigDecimal recipientNewBalance = recipientOldBalance.add(transaction.getAmount());

        createRecipientTransaction(transaction, recipientAccount);

        originalAccount.setBalance(newOriginalBalance);
        recipientAccount.setBalance(recipientNewBalance);
        transaction.setStatus(TransactionStatus.COMPLETED);

        accountRepository.save(originalAccount);
        accountRepository.save(recipientAccount);
        transactionRepository.save(transaction);
    }




    public void createRecipientTransaction(Transaction transaction, Account recipientAccount){
        Transaction recipientTransaction = new Transaction();
        recipientTransaction.setAccount(recipientAccount);
        recipientTransaction.setAmount(transaction.getAmount());
        recipientTransaction.setTransactionDate(LocalDateTime.now());
        recipientTransaction.setRecipientAccountNumber(recipientAccount.getAccountNumber());
        recipientTransaction.setDescription(transaction.getDescription());
        recipientTransaction.setType(transaction.getType());
        recipientTransaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(recipientTransaction);

    }

    // get all transactions
    public List<TransactionDTO> findAllTransactions(){
        return transactionRepository.findAll().stream()
                .map(this:: convertToDTO)
                .collect(Collectors.toList());
    }


    public List<User> manageUsers(Long accountId, String activeUsername){
        User activeUser = userRepository.findByUsername(activeUsername)
                .orElseThrow(() -> new NotFoundException("User not found."));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found."));

        logger.info(activeUser.getRole().toString());
        List<User> users = new ArrayList<>();

        users.add(activeUser);
        users.add(account.getUser());

        // Find user of account
        return users;

    }

    // get all transactions for account
    public List<TransactionDTO> findTransactionsByAccountId(Long accountId, String activeUserName) throws UnauthorizedException, NotFoundException{

        List<User> users = manageUsers(accountId, activeUserName);
        User activeUser = users.get(0);
        User user = users.get(1);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found."));

        // if role is USER, userId must be their own.  Cannot grab user ids for other accounts unless admin.
        loginService.checkPrivileges(user.getUserId(), activeUser);
        return transactionRepository.findTransactionsByAccount(account).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public TransactionDTO findByTransactionId(Long transactionId, String activeUsername) throws NotFoundException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("User Account with id: " + transactionId + " Not Found."));
        return convertToDTO(transaction);
    }




    // get all transactions for account by type
    public List<TransactionDTO> findTransactionsByAccountIdAndType(Long accountId, String activeUsername, TransactionType transactionType) throws UnauthorizedException, NotFoundException{
        List<User> users = manageUsers(accountId, activeUsername);
        User activeUser = users.get(0);
        User user = users.get(1);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found."));

        loginService.checkPrivileges(user.getUserId(), activeUser);
        return transactionRepository.findTransactionsByAccountAndType(account, transactionType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());



    }

    // get all transactions for account by date
    public List<TransactionDTO> findTransactionsByAccountIdAndDate(Long accountId, String activeUserName, LocalDate startDate, LocalDate endDate) throws UnauthorizedException, NotFoundException {
        List<User> users = manageUsers(accountId, activeUserName);
        User activeUser = users.get(0);
        User user = users.get(1);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found."));

        LocalDateTime startDateTime = startDate.atStartOfDay();

        LocalDateTime endDateTime = endDate.atStartOfDay();

        loginService.checkPrivileges(user.getUserId(), activeUser);
        return transactionRepository.findTransactionsByAccountAndDateRange(account, startDateTime, endDateTime).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());



    }










}
