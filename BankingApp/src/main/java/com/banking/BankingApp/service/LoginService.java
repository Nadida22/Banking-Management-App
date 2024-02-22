package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.LoginDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class LoginService {


    @Autowired
    UserRepository userRepository;


    public void authenticateUser(String username, String password, UserRole requiredRole){
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found."));
        if(!password.equals(foundUser.getPassword())){
            throw new UnauthorizedException("Incorrect Username or Password");
        }
        if(requiredRole == UserRole.ADMIN && foundUser.getRole() != UserRole.ADMIN){
            throw new UnauthorizedException("Admin Privileges Required.");
        }

    }

    public void checkPrivileges(Long userId, User authenticatedUser){
        if(!Objects.equals(userId, authenticatedUser.getUserId()) && authenticatedUser.getRole() != UserRole.ADMIN){
            throw new UnauthorizedException("Access Denied. You do not have correct Privileges to view Accounts for User Id: " + userId);
        }
    }



}
