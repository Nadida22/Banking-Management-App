package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.UserDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.validator.UserValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserValidator userValidator;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserDTO convertToDTO(User user){
        // new UserDTO
        Set<AccountDTO> accounts = new HashSet<>();
        UserDTO userDto = new UserDTO();
        userDto.setUserId(user.getUserId());
        userDto.setRole(user.getRole());
        userDto.setPassword(user.getPassword());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        if(user.getAccounts() != null){


            for(Account account: user.getAccounts()){
                AccountDTO newAccountDTO = new AccountDTO(
                        account.getAccountId(),
                        account.getAccountType(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getUser().getUserId()
                );
                accounts.add(newAccountDTO);

                logger.info(user.getAccounts().toString());


            }
            userDto.setAccounts(accounts);
        }




//         handle account DTOs



        return userDto;
    }


    public User convertToEntity(UserDTO userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setRole(userDto.getRole());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        Set<Account> accounts = getAccounts(userDto);
        user.setAccounts(accounts);

        return user;
    }

    private static Set<Account> getAccounts(UserDTO userDto) {
        Set<Account> accounts = new HashSet<>();

        if (userDto.getAccounts() != null) { // Check if accounts set is not null
            for (AccountDTO accountDto : userDto.getAccounts()) {
                Account newAccount = new Account(
                        accountDto.getAccountId(),
                        accountDto.getAccountType(),
                        accountDto.getAccountNumber(),
                        accountDto.getBalance()
                );
                accounts.add(newAccount);
            }
        }
        return accounts;
    }


    // validate User
    private void validateUser(User user) throws InvalidUserException{
        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new InvalidUserException("User is Invalid.", errors);
        }
    }


    // register new User

    public UserDTO registerUser(UserDTO userDto) throws InvalidUserException{
        // convert to entity
        User user = convertToEntity(userDto);
        // validate User Object
        validateUser(user);
        // Check that username doesn't exist
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            throw new InvalidUserException("Username already exists.");
        });

        // Check for other fields to ensure duplicate accounts don't exist for single person ????


        // return sanitized user object
        userRepository.save(user);
        return convertToDTO(user);



    }


    // get all users -- administrative
    @Transactional
    public List<UserDTO> findAllUsers(){
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    //  login
    public User loginUser(User user){

        return user;
    }


    public UserDTO findByUserId(String username, Long userId) throws NotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Account with id: " + userId + " Not Found."));
        if(!userId.equals(user.getUserId()) && user.getRole() == UserRole.USER){
            throw new UnauthorizedException("Cannot retrieve other user's credentials. Privileges Not Found.");
        }
        return convertToDTO(user);
    }


    public UserDTO updateUserDetails(UserDTO userDtoUpdates){
        // Administrative approval needed
        User userUpdates = convertToEntity(userDtoUpdates);
        validateUser(userUpdates);
        User existingUser = userRepository.findById(userUpdates.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        if(userUpdates.getPassword() != null){
            existingUser.setPassword(userUpdates.getPassword());
        }

        if(userUpdates.getRole() != null){
            existingUser.setRole(userUpdates.getRole());
        }
        if(userUpdates.getUsername() != null){
            existingUser.setUsername(userUpdates.getUsername());
        }
        if(userUpdates.getFirstName() != null){
            existingUser.setFirstName(userUpdates.getFirstName());
        }
        if(userUpdates.getLastName() != null){
            existingUser.setLastName(userUpdates.getLastName());
        }
        if(userUpdates.getEmail() != null){
            existingUser.setEmail(userUpdates.getEmail());
        }


        userRepository.save(existingUser);
        return convertToDTO(existingUser);

    }




    // update user credentials
    public UserDTO updateUserPassword(UserDTO userDtoUpdates) throws NotFoundException{
        // Can update password.

        User userUpdates = convertToEntity(userDtoUpdates);
        validateUser(userUpdates);
        User existingUser = userRepository.findById(userUpdates.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        if(userUpdates.getPassword() != null){
            existingUser.setPassword(userUpdates.getPassword());
        }
        userRepository.save(existingUser);
        return convertToDTO(existingUser);
    }


    // delete user -- admin only
    public boolean deleteUser(Long userId) throws UnauthorizedException{
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.deleteById(userId);
                    return true;})
                .orElse(false);


    }








}
