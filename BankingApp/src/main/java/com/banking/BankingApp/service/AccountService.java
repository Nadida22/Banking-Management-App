package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.InvalidAccountException;
import com.banking.BankingApp.exception.InvalidUserException;
import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.Account;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.AccountDTO;
import com.banking.BankingApp.model.dto.UserDTO;
import com.banking.BankingApp.model.enums.AccountType;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.AccountRepository;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.validator.AccountValidator;
import com.banking.BankingApp.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @Autowired
    AccountValidator accountValidator;
    @Autowired
    UserValidator userValidator;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }



    public AccountDTO convertToDTO(Account account) {
        AccountDTO accountDto = new AccountDTO();
        accountDto.setAccountId(account.getAccountId());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());

        if (account.getUser() != null) {
            accountDto.setUserId(account.getUser().getUserId());
        }
        // handle transaction DTOs
        return accountDto;

    }


    public Account convertToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountId(accountDTO.getAccountId());
        account.setAccountType(accountDTO.getAccountType());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setBalance(accountDTO.getBalance());

        if (accountDTO.getUserId() != null) {
            User user = userRepository.findById(accountDTO.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            account.setUser(user);
        }

        return account;
    }


    // Checking for Valid account
    private void validateAccount(Account account){
        Errors errors = new BeanPropertyBindingResult(account, "account");
        accountValidator.validate(account, errors);
        if (errors.hasErrors()) {
            throw new InvalidAccountException("Account is Invalid.", errors);
        }
    }

    private void validateUser(User user){

        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new InvalidAccountException("User is Invalid.", errors);
        }
    }

    public void generateAccountNumber(Account account) {
        // ensures account number is unique for newly created accounts.
        Long accountNumber;
        boolean isUnique;
        do {
            accountNumber = Account.generateUniqueAccountNumber();
            Optional<Account> existingAccount = accountRepository.findByAccountNumber(accountNumber);
            isUnique = existingAccount.isEmpty();

        } while (!isUnique);


        account.setAccountNumber(accountNumber);
        logger.info(accountNumber.toString());
    }


    // register new Account
    public AccountDTO registerAccount(AccountDTO accountDto) throws InvalidAccountException, NotFoundException {
        // Ensure user is created within the database.
        User user = userRepository.findById(accountDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not Found with Id. Account Not Created"));

        Account account = convertToEntity(accountDto);
        account.setUser(user);


        // Ensure account with account number does not exist.
        generateAccountNumber(account);
        // check account object is valid.
        validateAccount(account);

        // Ensures getAccountType() returns a valid enum.
        if(account.getAccountType() != AccountType.SAVINGS && account.getAccountType() != AccountType.CHECKING){
            throw new InvalidUserException("User AccountType is invalid");
        }
        accountRepository.save(account);

        return convertToDTO(account);
    }



    // retrieve Account (and balance details, other fields,  etc.)
    public AccountDTO findByAccountId(Long accountId) throws NotFoundException {
        // condensed the logic
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("There is no Account attached with " + accountId));
        return convertToDTO(account);
    }



    // retrieving all Accounts
    public List<AccountDTO> findAllAccounts(){
        return accountRepository.findAll().stream()
                // sanitize returned accounts
                .map(this:: convertToDTO)
                .collect(Collectors.toList());
    }



    // delete account -- admin only
    public boolean deleteAccount(Long accountId, UserDTO adminUserDto) throws UnauthorizedException {
        // Validate admin user
        User adminUser = userService.convertToEntity(adminUserDto);
        validateUser(adminUser);
        logger.info(adminUser.toString());

        // Authenticate admin user
        boolean isAuthenticated = userService.authenticateAdmin(adminUser);
        if(!isAuthenticated)
            throw new UnauthorizedException("Restricted. Privileges Not Found");

        // Check User exists. If so, then delete.
        return accountRepository.findById(accountId)
                .map(user -> {
                    userRepository.deleteById(accountId);
                    return true;})
                .orElse(false);


    }



    // retrieve all accounts for a user
    public List<AccountDTO> findAccountsByUserId(Long userId, UserDTO userDto) throws UnauthorizedException, NotFoundException{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // does user exist in database? Is password correct?
        boolean isAuthenticated = userService.authenticateUser(user);
        if(!isAuthenticated)
            throw new UnauthorizedException("Access Denied. Password or username not valid.");
        // if role is USER, userId must be their own.  Cannot grab user ids for other accounts unless admin.
        if(!Objects.equals(userId, user.getUserId()) && user.getRole() != UserRole.ADMIN){
            throw new UnauthorizedException("Access Denied. You do not have correct Privileges to view Accounts for User Id: " + userId);
        }
        return accountRepository.findAccountsByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public BigDecimal findTotalBalance(Long userId, UserDTO userDto){
      List<AccountDTO> accounts = findAccountsByUserId(userId, userDto);
      BigDecimal balance = new BigDecimal(0);
      for(AccountDTO account: accounts){
          balance = balance.add(account.getBalance());
      }
      return balance;
    }




    // update account


}
