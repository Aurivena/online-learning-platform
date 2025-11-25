package dev.aurivena.lms.domain.account;

public class Account {
    private String email;
    private String login;
    private String username;
    private String password;

    public Account(String email, String login, String username, String password) {
        this.email = email;
        this.login = login;
        this.username = username;
        this.password = password;
    }
}
