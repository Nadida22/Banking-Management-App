package com.banking.BankingApp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MappingController {
    @RequestMapping("/")
    public String home(){
        return "redirect:/html/homepage.html";
    }


    @RequestMapping("/user-signup")
    public String signUp(){
        return "redirect:/html/signup.html";
    }

    @RequestMapping("/user-login")
    public String logIn(){
        return "redirect:/html/login.html";
    }

    @RequestMapping("/accounts")
    public String accountCreate(){
        return "redirect:/html/createaccount.html";
    }
}
