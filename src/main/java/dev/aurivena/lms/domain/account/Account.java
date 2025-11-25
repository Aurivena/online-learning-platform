package dev.aurivena.lms.domain.account;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
class Account {
    private String email;
    private String login;
    private String username;
    private String passwordHash;

    public Account(String email, String login, String username, String passwordHash) {
        this.email = email;
        this.login = login;
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
