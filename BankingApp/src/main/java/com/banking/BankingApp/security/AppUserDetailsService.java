package com.banking.BankingApp.security;

import com.banking.BankingApp.exception.NotFoundException;
import com.banking.BankingApp.model.User;
import com.banking.BankingApp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {



    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found."));

        return new AppUserPrincipal(user);

    }
}
