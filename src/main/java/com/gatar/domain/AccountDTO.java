package com.gatar.domain;

/**
 * Data Transfer Object used to receive registration data from user phone.
 */
public class AccountDTO {

    private String username;

    private String password;

    private String email;

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

    public Account toAccount(){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail(email);
        return account;
    }
}
