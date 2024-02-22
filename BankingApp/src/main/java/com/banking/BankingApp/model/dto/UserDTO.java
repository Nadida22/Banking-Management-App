package com.banking.BankingApp.model.dto;
import com.banking.BankingApp.model.enums.UserRole;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserDTO {

    private long userId;
    private UserRole role;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<AccountDTO> accounts;

    public UserDTO() {
    }

    public UserDTO(long userId, UserRole role, String username, String email, String password, String firstName, String lastName, Set<AccountDTO> accounts) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }

    public UserDTO(UserRole role, String username, String email, String password, String firstName, String lastName, Set<AccountDTO> accounts) {
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }


    public UserDTO(UserRole role, String username, String email, String firstName, String lastName, Set<AccountDTO> accounts) {
        this.role = role;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }





    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public void addAccountToSet(AccountDTO accountDTO) {

        this.accounts.add(accountDTO);
    }

    public void removeAccountFromSet(AccountDTO accountDTO){
        this.accounts.remove(accountDTO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return userId == userDTO.userId && role == userDTO.role && Objects.equals(username, userDTO.username) && Objects.equals(email, userDTO.email) && Objects.equals(password, userDTO.password) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(accounts, userDTO.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, role, username, email, password, firstName, lastName, accounts);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accounts=" + accounts +
                '}';
    }


}
