package com.br.cdr.mercadobarato.model;

/**
 * Created by clebr on 20/02/2017.
 */

public class UserWrapper {

    private String username;
    private String password;
    private String token;
    private String first_name;
    private String email;

    public String getEmail() {
        return email;
    }

    public UserWrapper setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirst_name() {
        return first_name;
    }

    public UserWrapper setFirst_name(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserWrapper setToken(String token) {
        this.token = token;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserWrapper setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserWrapper setUsername(String username) {
        this.username = username;
        return this;
    }
}
