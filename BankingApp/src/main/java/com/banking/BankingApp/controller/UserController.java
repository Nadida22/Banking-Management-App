package com.banking.BankingApp.controller;
import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.dto.LoginDTO;
import com.banking.BankingApp.model.dto.TokenDTO;
import com.banking.BankingApp.model.dto.UserDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.service.LoginService;
import com.banking.BankingApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {



    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);






    // OK
    @PostMapping("/user")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDto){
            logger.info(userDto.toString());
            UserDTO response = userService.registerUser(userDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // OK
    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestBody TokenDTO<?> tokenDto){
        // Admin endpoint
        UserRole requiredRole = UserRole.ADMIN;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        List<UserDTO> response = userService.findAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // OK
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long userId, @RequestBody TokenDTO<?> tokenDto){
        // admin
        UserRole requiredRole = UserRole.USER;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        if(tokenDto.getUsername() == null){
            throw new InvalidAccountException("Username is Required.");
        }
        UserDTO response = userService.findByUserId(tokenDto.getUsername(), userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDto){
        long response = loginService.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), UserRole.USER);
        logger.info(String.valueOf(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // OK
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestBody TokenDTO<?> tokenDto){
        // Admin endpoint
        loginService.checkToken(tokenDto.getToken(), UserRole.ADMIN);
        boolean isDeleted = userService.deleteUser(userId);
        if(!isDeleted)
            throw new NotFoundException("User with id " + userId + " was not found.");

        return new ResponseEntity<>("{\"message\":\"Successfully Deleted\"}", HttpStatus.OK);


    }
    // OK
    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserDTO> updateUserPassword(@PathVariable("userId") Long userId, @RequestBody TokenDTO<UserDTO> tokenDto){
        UserRole requiredRole = UserRole.USER;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        UserDTO userDto = tokenDto.getData();
        userDto.setUserId(userId);
        UserDTO response = userService.updateUserPassword(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    // OK
    @PatchMapping("/user/admin/{userId}")
    public ResponseEntity<UserDTO> updateUserDetails(@PathVariable("userId") Long userId, @RequestBody TokenDTO<UserDTO> tokenDto){
        // Admin endpoint
        UserRole requiredRole = UserRole.ADMIN;
        loginService.checkToken(tokenDto.getToken(), requiredRole);
        UserDTO userDto = tokenDto.getData();
        userDto.setUserId(userId);
        UserDTO response = userService.updateUserDetails(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }





}
