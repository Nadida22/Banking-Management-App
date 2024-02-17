package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;


@Service
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
        validateUser(user);
        // Check that username doesn't exist
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            throw new InvalidUserException("Username already exists.");
        });

        // Check for other fields to ensure duplicate accounts don't exist for single person ????


        // return sanitized user object
        User savedUser = userRepository.save(user);
        return User.sanitize(user);



    }


    // get all users -- administrative
    public List<User> getAllUsers(){
        return userRepository.findAll().stream()
                .map(User::sanitize)
                .collect(Collectors.toList());
    }



    //  login
    public User loginUser(User user){

        return user;
    }


    // update user credentials
    public User updateUserDetails(User userUpdates){
        // Can update password.
        // Administrative approval will be needed for firstName and LastName
        validateUser(userUpdates);
        User existingUser = userRepository.findById(userUpdates.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        if(userUpdates.getPassword() != null){
            existingUser.setPassword(userUpdates.getPassword());
        }
        return userRepository.save(existingUser);
    }


    // delete user -- admin only
    public boolean deleteUser(Long userId, User adminUser){
        // Validate admin user
        validateUser(adminUser);

        // Authenticate admin user
        User foundAdminUser = userRepository.findById(adminUser.getUserId())
                .orElseThrow(() -> new UnauthorizedException("Unauthorized."));
        if(foundAdminUser.getRole()!= UserRole.ADMIN){
            throw new UnauthorizedException("Restricted. Privileges Not Found");
        }

        // Check User exists. If so, then delete.
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.deleteById(userId);
                    return true;})
                .orElse(false);


    }





}
