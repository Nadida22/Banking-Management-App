package com.banking.BankingApp.service;

import com.banking.BankingApp.controller.UserController;
import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.UserDTO;
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
    AccountValidator accountValidator;
    @Autowired
    UserValidator userValidator;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
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
    public Account registerAccount(Account account) throws InvalidAccountException, NotFoundException {
        // Ensure account with account number does not exist.
        generateAccountNumber(account);
        // check account object is valid.
        validateAccount(account);

        // Ensure user is created within the database.
        userRepository.findById(account.getUser().getUserId())
                .orElseThrow(() -> new NotFoundException("User not Found with Id. Account Not Created"));


        // Ensures getAccountType() returns a valid enum.
        if(account.getAccountType() != AccountType.SAVINGS && account.getAccountType() != AccountType.CHECKING){
            throw new InvalidUserException("User AccountType is invalid");
        }
        return accountRepository.save(account);
        // TODO: needs to be sanitized
    }



    // retrieve Account (and balance details, other fields,  etc.)
    public Account findByAccountId(Long accountId) throws NotFoundException {
        // condensed the logic
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("There is no Account attached with " + accountId));
    }



    // retrieving all Accounts
    public List<Account> findAllAccounts(){
        return accountRepository.findAll().stream()
                // sanitize returned accounts
                .map(Account::sanitize)
                .collect(Collectors.toList());
    }


    // close Account



    // retrieve all accounts for a user
    public List<Account> findAccountsByUser(User user) throws NotFoundException{
        validateUser(user);
        return accountRepository.findAccountsByUser(user).stream()
                .map(Account::sanitize)
                .collect(Collectors.toList());
    }


}
