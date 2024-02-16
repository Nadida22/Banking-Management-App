package com.banking.BankingApp.validator;

import com.banking.BankingApp.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.lang.NonNull;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;

        // Validate username
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.empty", "Username cannot be empty");
        }

        // Validate password
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty", "Password cannot be empty");
        }

        // Validate email
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.rejectValue("email", "email.empty", "Email cannot be empty");
        } else if (!user.getEmail().contains("@")) {
            errors.rejectValue("email", "email.invalid", "Email is invalid");
        }

        // Validate firstName
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            errors.rejectValue("firstName", "firstName.empty", "First name cannot be empty");
        }

        // Validate lastName
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            errors.rejectValue("lastName", "lastName.empty", "Last name cannot be empty");
        }

    }
}