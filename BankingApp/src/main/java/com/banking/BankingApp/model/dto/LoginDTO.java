package com.banking.BankingApp.model.dto;

public class LoginDTO<T> {


    private T data;
    private String username;

    private String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;
    }


}
