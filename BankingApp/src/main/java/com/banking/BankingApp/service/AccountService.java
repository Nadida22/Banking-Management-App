package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.Transaction;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.TransactionDTO;
import com.banking.BankingApp.model.enums.AccountType;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.validator.AccountValidator;
import com.banking.BankingApp.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;


    @Autowired
    AccountValidator accountValidator;
    @Autowired
    UserValidator userValidator;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }



    public AccountDTO convertToDTO(Account account) {
        Set<TransactionDTO> transactions = new HashSet<>();
        AccountDTO accountDto = new AccountDTO();
        accountDto.setAccountId(account.getAccountId());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());
        if (account.getUser() != null) {
            accountDto.setUserId(account.getUser().getUserId());
        }
        // handle transaction DTOs
        if(account.getTransactions() != null){


            for(Transaction transaction: account.getTransactions()){
                TransactionDTO transactionDto = new TransactionDTO();
                transactionDto.setTransactionId(transaction.getTransactionId());
                transactionDto.setType(transaction.getType());
                transactionDto.setTransactionDate(transaction.getTransactionDate());
                transactionDto.setAmount(transaction.getAmount());
                transactionDto.setRecipientAccountNumber(transaction.getRecipientAccountNumber());
                transactionDto.setDescription(transaction.getDescription());
                transactionDto.setAccountId(transaction.getAccount().getAccountId());
                transactionDto.setStatus(transaction.getStatus());
                transactions.add(transactionDto);

                logger.info(account.getTransactions().toString());
    }
            accountDto.setTransactions(transactions);

        }
        return accountDto;

    }


    public Account convertToEntity(AccountDTO accountDto) {
        Account account = new Account();
        account.setAccountId(accountDto.getAccountId());
        account.setAccountType(accountDto.getAccountType());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
        Set<Transaction> transactions = getTransactions(accountDto);

        if (accountDto.getUserId() != null) {
            User user = userRepository.findById(accountDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            account.setUser(user);
        }

        return account;
    }

    private static Set<Transaction> getTransactions(AccountDTO accountDto) {
        Set<Transaction> transactions = new HashSet<>();


        if (accountDto.getTransactions() != null) { // Check if accounts set is not null
            for (TransactionDTO transactionDto : accountDto.getTransactions()) {
                Transaction newTransaction = new Transaction(
                        transactionDto.getTransactionId(),
                        transactionDto.getType(),
                        transactionDto.getAmount(),
                        transactionDto.getTransactionDate(),
                        transactionDto.getStatus(),
                        transactionDto.getRecipientAccountNumber(),
                        transactionDto.getDescription());

                transactions.add(newTransaction);
            }
        }
        return transactions;
    }




    // Checking for Valid account
    private void validateAccount(Account account){
        Errors errors = new BeanPropertyBindingResult(account, "account");
        accountValidator.validate(account, errors);
        if (errors.hasErrors()) {
            throw new InvalidAccountException("Account is Invalid.", errors);
        }
    }

    private void validateUser(User user){

        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new InvalidAccountException("User is Invalid.", errors);
        }
    }

    public void generateAccountNumber(Account account) {
        // ensures account number is unique for newly created accounts.
        Long accountNumber;
        boolean isUnique;
        do {
            accountNumber = Account.generateUniqueAccountNumber();
            Optional<Account> existingAccount = accountRepository.findByAccountNumber(accountNumber);
            isUnique = existingAccount.isEmpty();

        } while (!isUnique);


        account.setAccountNumber(accountNumber);
        logger.info(accountNumber.toString());
    }


    // register new Account
    public AccountDTO registerAccount(AccountDTO accountDto) throws InvalidAccountException, NotFoundException {
        // Ensure user is created within the database.
        User user = userRepository.findById(accountDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not Found with Id. Account Not Created"));

        Account account = convertToEntity(accountDto);
        account.setUser(user);


        // Ensure account with account number does not exist.
        generateAccountNumber(account);
        // check account object is valid.
        validateAccount(account);

        // Ensures getAccountType() returns a valid enum.
        if(account.getAccountType() != AccountType.SAVINGS && account.getAccountType() != AccountType.CHECKING){
            throw new InvalidUserException("User AccountType is invalid");
        }
        accountRepository.save(account);

        return convertToDTO(account);
    }



    // retrieve Account (and balance details, other fields,  etc.)
    public AccountDTO findByAccountId(Long accountId, String activeUsername) throws NotFoundException {

        User activeUser = userRepository.findByUsername(activeUsername)
                .orElseThrow(() -> new NotFoundException("User not found."));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("There is no Account attached with " + accountId));
        logger.info(activeUser.getRole().toString());
        logger.info(activeUser.getUserId().toString());
        logger.info(account.getUser().getUserId().toString());

        loginService.checkPrivileges(account.getUser().getUserId(), activeUser);


        return convertToDTO(account);
    }



    // retrieving all Accounts
    public List<AccountDTO> findAllAccounts(){
        return accountRepository.findAll().stream()
                // sanitize returned accounts
                .map(this:: convertToDTO)
                .collect(Collectors.toList());
    }



    // delete account -- admin only
    public boolean deleteAccount(Long accountId) throws UnauthorizedException {
        return accountRepository.findById(accountId)
                .map(user -> {
                    userRepository.deleteById(accountId);
                    return true;})
                .orElse(false);


    }



    // retrieve all accounts for a user
    public List<AccountDTO> findAllAccountsByUserId(Long userId, String activeUserName) throws UnauthorizedException, NotFoundException{
        User activeUser = userRepository.findByUsername(activeUserName)
        .orElseThrow(() -> new NotFoundException("User not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        logger.info(activeUser.getRole().toString());

        // if role is USER, userId must be their own.  Cannot grab user ids for other accounts unless admin.
        loginService.checkPrivileges(userId, activeUser);
        return accountRepository.findAccountsByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }





    public BigDecimal findTotalBalance(Long userId, String username){
      List<AccountDTO> accounts = findAllAccountsByUserId(userId, username);
      BigDecimal balance = new BigDecimal(0);
      for(AccountDTO account: accounts){
          balance = balance.add(account.getBalance());
      }
      return balance;
    }




    // update account


}
