package com.banking.BankingApp;

import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.UserRepository;
import com.banking.BankingApp.service.UserService;
import com.banking.BankingApp.validator.UserValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTests {



    @Mock
    private UserRepository userRepository;


    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;


    public User registerUserTest_Success(User user){




        verify(userValidator).validate(any(User.class), any(Errors.class));

        return user;
    }

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
    public User updateUserDetailsTest_Success(User user){

        return user;
    }


    public User updateUserDetailsTest_NotFound(User user){

        return user;
    }


    // delete user -- admin only
    public void deleteUserTest_Success(){
        User user = new User();
        user.setUserId(1L);
        User adminUser = new User();
        adminUser.setUserId(2L);
        adminUser.setRole(UserRole.ADMIN);
        // Validate that both users exist in the database.
        when(userRepository.findById(adminUser.getUserId())).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));


        doNothing().when(userRepository).deleteById(user.getUserId());
        boolean isDeleted = userService.deleteUser(user.getUserId(), adminUser);
        Optional<User> foundAdminUser = userRepository.findById(adminUser.getUserId());

        // Assert if Admin User is returned from database and Admin is an admin.
        assertTrue(isDeleted, "User Should be Successfully Deleted.");
        // Validate that the adminUser is an admin with privileges.
        assertTrue(foundAdminUser.isPresent() && UserRole.ADMIN.equals(foundAdminUser.get().getRole()));
    }


    // delete user -- admin only
    public boolean deleteUserTest_NotFound(User userToDelete, User adminUser){


        return true;
    }

}
