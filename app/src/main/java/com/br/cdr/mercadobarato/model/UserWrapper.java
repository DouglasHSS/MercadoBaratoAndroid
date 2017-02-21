package com.br.cdr.mercadobarato.model;

/**
 * Created by clebr on 20/02/2017.
 */

public class UserWrapper {

    private String mUsername;
    private String mPassword;
    private String mToken;
    private String mFirstName;
    private String mEmail;

    public String getUsername() {
        return mUsername;
    }

    public UserWrapper setUsername(String mUsername) {
        this.mUsername = mUsername;
        return this;
    }

    public String getPassword() {
        return mPassword;
    }

    public UserWrapper setPassword(String mPassword) {
        this.mPassword = mPassword;
        return this;
    }

    public String getToken() {
        return mToken;
    }

    public UserWrapper setToken(String mToken) {
        this.mToken = mToken;
        return this;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public UserWrapper setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
        return this;
    }

    public String getmEmail() {
        return mEmail;
    }

    public UserWrapper setmEmail(String mEmail) {
        this.mEmail = mEmail;
        return this;
    }
}
