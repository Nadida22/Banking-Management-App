package com.banking.BankingApp.model;
import com.banking.BankingApp.model.enums.UserRole;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name="AppUser")
public class User {

    // userid -- long - unique identifier
    // role -- enum -  to distinguish between user role or admin role
    // username -- String - self-explanatory
    // password -- String
    // email -- String
    // firstname -- String
    // lastname -- String
    // accounts -- HashSet -- OnetoMany relationship to Account
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Enumerated(EnumType.STRING)

    @Column(name="UserRole")
    private UserRole role;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();


    public User() {
    }

    public User(UserRole role, String username, String password, String email, String firstName, String lastName, Set<Account> accounts) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }


    public User(UserRole role, String username, String password, String email, String firstName, String lastName) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(Long userId, UserRole role, String username, String password, String email, String firstName, String lastName, Set<Account> accounts) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccountToList(Account account) {

        this.accounts.add(account);
    }

    public void removeAccountFromList(Account account){
        this.accounts.remove(account);
    }




    public static User sanitize(User user) {
        if (user == null) {
            return null;
        }
    User sanitizedUser = new User();
        sanitizedUser.setUserId(user.getUserId());
        sanitizedUser.setRole(user.getRole());
        sanitizedUser.setUsername(user.getUsername());
    // Do not set the password
        sanitizedUser.setEmail(user.getEmail());
        sanitizedUser.setFirstName(user.getFirstName());
        sanitizedUser.setLastName(user.getLastName());
    // Do not set the accounts as they might contain sensitive data

        return sanitizedUser;
}



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && role == user.role && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(accounts, user.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, role, username, password, email, firstName, lastName, accounts);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
