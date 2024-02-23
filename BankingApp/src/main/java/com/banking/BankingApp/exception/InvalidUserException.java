package com.banking.BankingApp.exception;





import org.springframework.validation.Errors;

public class InvalidUserException extends RuntimeException{

    private Errors errors;

    public InvalidUserException(String msg, Errors errors){
        super(msg);
        this.errors = errors;
    }

    public InvalidUserException(String msg){
        super(msg);
    }

    public Errors getErrors(){
        return errors;
    }


}