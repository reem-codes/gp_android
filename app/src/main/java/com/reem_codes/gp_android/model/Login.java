package com.reem_codes.gp_android.model;

public class Login {
    private String message;
    private String access_token;
    private User user;

    public Login(String access_token, User user, String message) {
        this.access_token = access_token;
        this.message = message;
        this.user = user;
    }

    public Login() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
