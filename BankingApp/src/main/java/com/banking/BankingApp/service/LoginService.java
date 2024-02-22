package com.banking.BankingApp.service;

import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.exception.UnauthorizedException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.model.dto.LoginDTO;
import com.banking.BankingApp.model.enums.UserRole;
import com.banking.BankingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;


@Service
public class LoginService {


    @Autowired
    UserRepository userRepository;

    private static long USERCODE = 1000055001;
    private static long ADMINCODE = 1000055002;

    private static final Map<UserRole, Long> ROLE_TOKEN_MAP = new EnumMap<>(UserRole.class);

    static {
        // Initialize the map with role-token pairs
        ROLE_TOKEN_MAP.put(UserRole.USER, USERCODE);
        ROLE_TOKEN_MAP.put(UserRole.ADMIN, ADMINCODE);
        // Add more roles here if needed
    }


    public long authenticateUser(String username, String password, UserRole requiredRole) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found."));
        if (!password.equals(foundUser.getPassword())) {
            throw new UnauthorizedException("Incorrect Username or Password");
        }
        if (requiredRole == UserRole.ADMIN && foundUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Admin Privileges Required.");
        }
        return getAuthenticationCode(foundUser.getRole());

    }

    public void checkPrivileges(Long userId, User authenticatedUser) {
        if (!Objects.equals(userId, authenticatedUser.getUserId()) && authenticatedUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Access Denied. You do not have correct Privileges for : " + userId);
        }
    }


    public long getAuthenticationCode(UserRole role) {
        return switch (role) {
            case ADMIN -> ADMINCODE;
            case USER -> USERCODE;
            // Add more cases for other roles if necessary
            default -> throw new IllegalArgumentException("Unknown user role: " + role);

            // Or return a default code if that's more appropriate for your application
        };
    }

    public boolean checkToken(Long token, UserRole role) {
        // Check if the token matches the expected token for the given role
        if (token.equals(ROLE_TOKEN_MAP.get(role))) {
            return true;
        }

        // Additionally, allow ADMIN role to access USER role resources
        if (role == UserRole.USER && token.equals(ROLE_TOKEN_MAP.get(UserRole.ADMIN))) {
            return true;
        }

        throw new UnauthorizedException("Not authorized. Token does not match the expected token for role: " + role);
    }

}




