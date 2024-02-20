package com.banking.BankingApp.exception;
import org.springframework.validation.Errors;

public class InvalidTransactionException extends RuntimeException{

    private Errors errors;

    public InvalidTransactionException(String msg, Errors errors){
        super(msg);
        this.errors = errors;
    }

    public InvalidTransactionException(String msg){
        super(msg);
    }

}
