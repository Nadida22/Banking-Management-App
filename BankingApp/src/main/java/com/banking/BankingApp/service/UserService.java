package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.exception.UserNotFoundException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserValidator userValidator;



    // validate User
    private void validateUser(User user) {
        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new InvalidUserException("User is Invalid.", errors);
        }
    }


    // register new User

    public User registerUser(User user){
        // validate User Object


        return user;
    }


    //  login
    public User loginUser(User user){

        return user;
    }


    // update user credentials
    public User updateUserDetails(User user){

        return user;
    }


    // delete user -- admin only
    public boolean deleteUser(Long userId, User adminUser){
        // Validate admin user
        validateUser(adminUser);

        // Authenticate admin user
        User foundAdminUser = userRepository.findById(adminUser.getUserId())
                .orElseThrow(() -> new UnauthorizedException("Unauthorized."));

        // Check User exists. If so, then delete.
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.deleteById(userId);
                    return true;})
                .orElse(false);


    }





}
