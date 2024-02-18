package com.banking.BankingApp.controller;


import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.UserDTO;
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
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDto){
            logger.info(userDto.toString());
            UserDTO response = userService.registerUser(userDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers(){
        List<UserDTO> response = userService.findAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        UserDTO response = userService.findByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestBody User adminUser){
        boolean isDeleted = userService.deleteUser(userId, adminUser);
        if(!isDeleted)
            throw new NotFoundException("User with id " + userId + " was not found.");

        return new ResponseEntity<>("{\"message\":\"Successfully Deleted\"}", HttpStatus.OK);


    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDto){
        userDto.setUserId(userId);
        UserDTO response = userService.updateUserDetails(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // login



}
