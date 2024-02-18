package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.enums.AccountType;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.validator.AccountValidator;
import com.banking.BankingApp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    AccountRepository accRepo;

    @Autowired
    AccountValidator accountValidator;
    @Autowired
    UserValidator userValidator;

    public AccountService(AccountRepository accRepo){
        this.accRepo = accRepo;
    }


    // Checking for Valid account
    private void checkAccount(Account account){

        Errors errors = new BeanPropertyBindingResult(account, "account");
        accountValidator.validate(account, errors);
        if (errors.hasErrors()) {
            throw new InvalidAccountException("Account is Invalid.", errors);
        }
    }

    private void checkUser(User user){

        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new InvalidAccountException("User is Invalid.", errors);
        }
    }

    // register new Account
    public Account registerAccount(Account account) throws InvalidAccountException {

        checkAccount(account);
        if(account.getAccountType() != AccountType.SAVINGS || account.getAccountType() != AccountType.CHECKING){
            throw new InvalidUserException("User AccountType is invalid");
        }
        return accRepo.save(account);
    }



    // retrieve Account (and balance details, other fields,  etc.)
    public Account findAccountByAccountId(Long accountId) throws NotFoundException {

        Optional<Account> checkId = accRepo.findById(accountId);

        if(checkId.isPresent()){
            return checkId.get();
        } else{
            throw new NotFoundException("There is no Account attached with " + accountId);
        }
    }



    // retrieving all Accounts
    public List<Account> findAllAccounts(){
        return accRepo.findAll().stream()
                // sanitize returned accounts
                .map(Account::sanitize)
                .collect(Collectors.toList());
    }


    // close Account



    // retrieve all accounts for a user

    public List<Account> findAccountsByUser(User user) throws NotFoundException{

        checkUser(user);
        Optional<Account> findAccount = accRepo.findAccountsByUser(user);

        if(!findAccount.isPresent()){
            throw new NotFoundException();
        }

        return findAccount.stream().toList();

    }


}
