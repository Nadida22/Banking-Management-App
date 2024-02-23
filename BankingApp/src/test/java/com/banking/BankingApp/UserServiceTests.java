package com.banking.BankingApp;

import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.UserDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.service.UserService;
import com.banking.BankingApp.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {



    @Mock
    private UserRepository userRepository;


    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private User user;
    private User adminUser;


    @BeforeEach
    void setup(){
        user = new User();

        user.setUserId(1L);
        user.setRole(UserRole.USER);
        user.setUsername("user001");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setEmail("user@example.com");

        adminUser = new User();

        adminUser.setUserId(2L);
        adminUser.setRole(UserRole.ADMIN);
        adminUser.setUsername("adminuser");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setPassword("password");
        adminUser.setEmail("admin@example.com");


    }


//    @Test
//    void registerUserTest_Success(){
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        UserDTO response = userService.registerUser(user);
//
//        verify(userValidator).validate(any(User.class), any(Errors.class));
//        assertNotNull(response);
//        assertEquals(user.getUserId(), response.getUserId());
//
//
//    }

    public User registerUserTest_EmptyUsername(User user){

        return user;
    }

    public User registerUserTest_EmptyFirstName(User user){

        return user;
    }


    //  login
    public User loginUserTest_Success(User user){


        return user;
    }


    public User loginUserTest_Unsuccessful(User user){

        return user;
    }


    // update user credentials
//    public User updateUserDetailsTest_Success(User user){
//
//        return user;
//    }


//    public User updateUserDetailsTest_NotFound(User user){
//
//        return user;
//    }


    // delete user -- admin only
//    @Test
//    void deleteUserTest_Success(){
//        // Validate that both users exist in the database.
//        when(userRepository.findById(adminUser.getUserId())).thenReturn(Optional.of(adminUser));
//        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
//
//
//        doNothing().when(userRepository).deleteById(user.getUserId());
//
//        boolean isDeleted = userService.deleteUser(user.getUserId(), adminUser);
//        Optional<User> foundAdminUser = userRepository.findById(adminUser.getUserId());
//
//        // Assert if Admin User is returned from database and Admin is an admin.
//        assertTrue(isDeleted, "User Should be Successfully Deleted.");
//
//        // Validate that the adminUser is an admin with privileges.
//        assertTrue(foundAdminUser.isPresent() && UserRole.ADMIN.equals(foundAdminUser.get().getRole()));
//    }
//
//
//    // delete user -- admin only
//    public boolean deleteUserTest_NotFound(User userToDelete, User adminUser){
//
//
//        return true;
//    }
//
}
