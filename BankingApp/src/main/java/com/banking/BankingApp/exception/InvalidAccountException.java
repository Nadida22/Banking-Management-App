package com.banking.BankingApp.exception;

import org.springframework.validation.Errors;

public class InvalidAccountException extends RuntimeException{

    private Errors errors;

    public InvalidAccountException(String msg, Errors errors){
        super(msg);
        this.errors = errors;
    }

    public InvalidAccountException(String msg){
        super(msg);
    }

}
