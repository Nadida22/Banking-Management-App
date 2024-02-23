package com.banking.BankingApp.model.dto;

public class TokenDTO<T> {

    public TokenDTO() {
    }

    public TokenDTO(int token) {
        this.token = token;
    }

    public TokenDTO(long token, T data) {

        this.token = token;
        this.data = data;
    }


    public TokenDTO(String username, long token, T data) {
        this.username = username;
        this.token = token;
        this.data = data;
    }

    private long token;



    private String username;
    private T data;



    public T getData(){
        return data;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }


    public void setData(T data){
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}

