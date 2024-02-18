package com.banking.BankingApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/", "/home").permitAll()
                                .requestMatchers("/user/**").permitAll()
                                .requestMatchers("/account/**").permitAll()
                                .anyRequest().authenticated()
                );
//                .formLogin((form) ->
//                        form
//                                .loginPage("/login")
//                                .permitAll()
//                )
//                .logout((logout) ->
//                        logout.permitAll()
//                );
        return http.build();
    }
}