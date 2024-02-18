package com.banking.BankingApp.controller;


import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;



    @PostMapping("/user")
    public ResponseEntity<User> registerUser(@RequestBody User user){
            logger.info(user.toString());
            User response = userService.registerUser(user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers(){
        List<User> response = userService.findAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/user")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        User response = userService.findByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @DeleteMapping("/userId")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestBody User adminUser){
        boolean isDeleted = userService.deleteUser(userId, adminUser);
        if(!isDeleted)
            throw new NotFoundException("User with id " + userId + " was not found.");

        return new ResponseEntity<>("{\"message\":\"Successfully Deleted\"}", HttpStatus.OK);


    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @RequestBody User user){
        user.setUserId(userId);
        User response = userService.updateUserDetails(user);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // login



}
