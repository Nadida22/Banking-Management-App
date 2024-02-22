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
        return "redirect:/html/user/signup.html";
    }

    @RequestMapping("/user-login")
    public String logIn(){
        return "redirect:/html/user/login.html";
    }

    @RequestMapping("/accounts")
    public String accountCreate(){
        return "redirect:/html/account/createaccount.html";
    }

    @RequestMapping("/customer-help")
    public String customerHelp(){
        return "redirect:/html/customerhelp.html";
    }


    @RequestMapping("/user-account")
    public String userAccount(){
        return "redirect:/html/account/accounts.html";
    }

    @RequestMapping("/user-portal")
    public String userPortal(){return "redirect:/html/user/user-portal.html";}








}
