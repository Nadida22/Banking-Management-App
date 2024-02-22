package com.banking.BankingApp.validator;

import com.banking.BankingApp.model.Transaction;
import com.banking.BankingApp.model.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class TransactionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Transaction transaction = (Transaction) target;

        // Validate amount
        if (transaction.getAmount().equals(BigDecimal.valueOf(0))){
            errors.rejectValue("amount", "amount.is.none", "Amount is Zero.");
        }
        if(transaction.getAccount() == null){
            errors.rejectValue("account", "account.is.empty", "Must have an Account Attached.");
        }

    }
}