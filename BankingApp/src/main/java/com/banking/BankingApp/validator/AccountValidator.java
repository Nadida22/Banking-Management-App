package com.banking.BankingApp.validator;

import com.banking.BankingApp.model.Account;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, Errors errors) {

        Account account = (Account) target;

        if(account.getAccountNumber() == null){
            errors.rejectValue("accountNumber", "accountNumber.empty", "accountNumber should be non empty");
        }

        if(account.getBalance() == null){
            errors.rejectValue("balance", "balance.empty", "balance should be non empty");
        }

        if(account.getUser() == null){
            errors.rejectValue("user", "user.empty", "user shouldn't be empty");
        }
    }
}
